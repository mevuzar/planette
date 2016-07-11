package fddd.book.chapter_five.free_monads.noel_welsh_example

import fddd.book.chapter_five.free_monads.noel_welsh_example.AccountRepository.AccountTerms.{Account, DeleteAccount, Fetch, Pure, QueryAccount, Request, Requestable, Service, StoreAccount}
import fddd.book.chapter_five.free_monads.noel_welsh_example.AccountRepository.ToyInterpreter

import scala.collection.mutable
import scala.util.Try
import scalaz.{Coyoneda, Free, Id, ~>}
import java.util.UUID

/**
 * @author yoav @since 7/10/16.
 */
object AccountRepository {

  object AccountTerms {

    type UserId = Int
    type UserName = String
    type UserToken = String

    type Requestable[A] = Coyoneda[Request, A]

    // this is described below

    final case class Account(userId: UserId, username: UserName, password: String)

    final case class User(id: UserId, name: UserName, photo: UserToken)

    // Services represent web services we can call to fetch data
    sealed trait Service[A]

    final case class QueryAccount(userId: UserId) extends Service[Try[Account]]

    final case class StoreAccount(account: Account) extends Service[Try[Unit]]

    final case class DeleteAccount(userId: UserId) extends Service[Try[Unit]]

    // A request represents a request for data
    sealed trait Request[A]

    final case class Pure[A](a: A) extends Request[A]

    final case class Fetch[A](service: Service[A]) extends Request[A]

  }

  object Request {
    def pure[A](a: A): Free[Requestable, A] =
      Free.liftFC(Pure(a): Request[A])

    def fetch[A](service: Service[A]): Free[Requestable, A] =
      Free.liftFC(Fetch(service): Request[A])
  }

  object ToyInterpreter extends (Request ~> Id.Id) {

    import Id._
    import AccountTerms._

    val map = mutable.HashMap.empty[UserId, (UserToken, Account)]

    def apply[A](in: Request[A]): Id[A] =
      in match {
        case Pure(a) => a
        case Fetch(service) =>
          service match {
            case QueryAccount(userId) =>

              println(s"Getting account for user id: $userId")
              Try{map.find(a => a._1 == userId).get._2._2}

            case StoreAccount(account) =>
              println(s"Storing user account $account")
              val tryIt = Try{
                val token = UUID.randomUUID.toString
                map.put(account.userId, (token, account))
                ()
              }

              tryIt

            case DeleteAccount(userId) =>
              Try {
                println(s"Deleting user account with id: $userId")
                map.remove(userId)
                ()
              }
          }
      }
  }

}

trait AccountRepositoryImpl{
    import AccountRepository.{AccountTerms, Request}
    import AccountTerms._
    import Request._

  def run[A](free:Free[Requestable,A], interpreter: Request ~> Id.Id): A =
      Free.runFC(free)(interpreter)
}
//trait ServiceOrchestrationImpl{
//  import AccountRepository.{AccountTerms, Request}
//  import AccountTerms._
//  import Request._
//
//  val theId: UserId = 1
//
//  def getUser(id: UserId): Free[Requestable, User] =
//    for {
//      name  <- fetch(GetUserName(id))
//      photo <- fetch(GetUserPhoto(id))
//    } yield User(id, name, photo)
//
//  val free: Free[Requestable, List[(String, User)]] =
//    for {
//      tweets <- fetch(GetTweets(theId))
//      result <- (tweets map { tweet: Tweet =>
//        for {
//          user <- getUser(tweet.userId)
//        } yield (tweet.msg -> user)
//      }).sequenceU
//    } yield result
//
//  def run(interpreter: Request ~> Id.Id): List[(String, User)] =
//    Free.runFC(free)(interpreter)
//}

object NoelsRepositoryFreeMonad extends AccountRepositoryImpl with App{
  import AccountRepository.{AccountTerms, Request}
  import AccountTerms._
  import Request._
  val account = Account(1, "Moshe", "Buhbut")
  val script: Free[Requestable, List[Try[Any]]] = for{
    stored <- fetch(StoreAccount(account))
    query <- fetch(QueryAccount(1))
    delete <- fetch(DeleteAccount(1))
  } yield List(stored, query, delete)
  
  val scriptResult = run[List[Try[Any]]](script , ToyInterpreter)
  scriptResult.foreach(println)
  println(ToyInterpreter.map)
}

