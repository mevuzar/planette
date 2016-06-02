package fddd.book.chapter_four
package monads.state_monad

import java.util.Date

import scala.util.{Try, Success}


/**
 * @author yoav @since 6/1/16.
 */
object WhileTKeyGenerator {
  val StateGen = scalaz.StateT.stateMonad[KeyGenerator]
  import StateGen._

  val repo = new AccountRepository {override def store(a: Account): Try[Account] = ???

    override def query(no: String): Try[Option[Account]] = ???

    override def query(openedOn: Date): Try[Seq[Account]] = ???

  }

  val s = whileM_(gets(_.exists), modify(_ => new KeyGenerator(repo)))
  val start = new KeyGenerator(repo)
  s exec start
}

final class KeyGenerator(rep: AccountRepository){
  val no = scala.util.Random.nextString(10)
  def exists: Boolean = {
    rep.query(no) match{
      case Success(Some(acc)) => true
      case _ => false
    }
  }
}