package fddd.book.chapter_six.ex_6_2

import scalaz._
import Scalaz._
import scalaz.syntax._
import \/._
/**
 * Created by Owner on 6/9/2016.
 */
object App extends App with ToEitherOps{
  val openAccount: Valid[Account] = AccountService.open("12345", "yoav", Some(23), None, Checking).run(AccountRepositoryInMemory)
  val accountEither: NonEmptyList[String] \/ Account = openAccount.run
  val account = ToEitherOps(accountEither).right
  println(account)
}
