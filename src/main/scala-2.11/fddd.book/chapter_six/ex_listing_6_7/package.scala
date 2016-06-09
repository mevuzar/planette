package fddd.book.chapter_six

import java.util.{Calendar, Date}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scalaz.Kleisli

/**
 * @author yoav @since 6/8/16.
 */
package object ex_listing_6_7 {
  type PFOperation = Kleisli[Future, Int, Seq[Int]]

  type Amount = BigDecimal

  def today = Calendar.getInstance.getTime

  sealed trait InstrumentType

  case object CCY extends InstrumentType
  case object EQ extends InstrumentType
  case object FI extends InstrumentType

  sealed trait Instrument {
    def instrumentType: InstrumentType
  }
  case class Account(no: String, name: String, dateOfOpen: Option[Date] = Some(today), dateOfClose: Option[Date], balance: Balance)
  case class Balance(ins: Instrument, holding: Amount, marketValue: Amount)
  trait Repository[A, IdType] {
    def query(id: IdType): Try[Option[A]]

    def store(a: A): Try[A]
  }

  trait AccountRepository extends Repository[Account, String] {
    def query(no: String): Try[Option[Account]]

    def store(a: Account): Try[Account]

    def balance(no: String): Try[Balance] = query(no) match {
      case Success(Some(a)) => Success(a.balance)
      case Success(None) => Failure(new Exception(s"No account exists with no $no"))
      case Failure(ex) => Failure(ex)
    }

    def query(openedOn: Date): Try[Seq[Account]]
  }

}
