package fddd.book.chapter_six

import java.util.{Date, Calendar}

import scala.util.{Failure, Success, Try}
import scalaz._
import Scalaz._
import scalaz.concurrent.Task

/**
 * Created by Owner on 6/9/2016.
 */
package object ex_6_2 {
  type Amount = BigDecimal
  type Valid[A] = Task[NonEmptyList[String] \/ A]
  type AccountOperation[A] = Kleisli[Valid, AccountRepository, A]

  def today = Calendar.getInstance.getTime

  sealed trait AccountType
  case object Checking extends AccountType
  case object Savings extends AccountType

  sealed trait InstrumentType

  case object CCY extends InstrumentType
  case object EQ extends InstrumentType
  case object FI extends InstrumentType

  sealed trait Instrument {
    def instrumentType: InstrumentType
  }

  case class Equity(isin: String, name: String, issueDate: Date, faceValue: Amount) extends Instrument {
    final val instrumentType = EQ
  }

  case class FixedIncome(isin: String, name: String, issueDate: Date, maturityDate: Option[Date],
                         nominal: Amount) extends Instrument {
    final val instrumentType = FI
  }

  case class Currency(isin: String) extends Instrument {
    final val instrumentType = CCY
  }

  case class Balance(amount: Amount = 0)

  sealed trait Account {
    def no: String
    def name: String
    def dateOfOpen: Option[Date]
    def dateOfClose: Option[Date]
    def balance: Balance
  }

  final case class CheckingAccount (no: String, name: String,
                                    dateOfOpen: Option[Date], dateOfClose: Option[Date] = None, balance: Balance = Balance()) extends Account

  final case class SavingsAccount (no: String, name: String, rateOfInterest: Amount,
                                   dateOfOpen: Option[Date], dateOfClose: Option[Date] = None, balance: Balance = Balance()) extends Account

  object Account {
    private def validateAccountNo(no: String) =
      if (no.isEmpty || no.size < 5) s"Account No has to be at least 5 characters long: found $no".failureNel[String]
      else no.successNel[String]

    private def validateOpenCloseDate(od: Date, cd: Option[Date]) = cd.map { c =>
      if (c before od) s"Close date [$c] cannot be earlier than open date [$od]".failureNel[(Option[Date], Option[Date])]
      else (od.some, cd).successNel[String]
    }.getOrElse { (od.some, cd).successNel[String] }

    private def validateRate(rate: BigDecimal) =
      if (rate <= BigDecimal(0)) s"Interest rate $rate must be > 0".failureNel[BigDecimal]
      else rate.successNel[String]

    def checkingAccount(no: String, name: String, openDate: Option[Date], closeDate: Option[Date],
                        balance: Balance): \/[NonEmptyList[String], Account] = {

      val od = openDate.getOrElse(today)

      (
        validateAccountNo(no) |@|
          validateOpenCloseDate(openDate.getOrElse(today), closeDate)
        ) { (n, d) =>
        CheckingAccount(n, name, d._1, d._2, balance)
      }.disjunction
    }

    def savingsAccount(no: String, name: String, rate: BigDecimal, openDate: Option[Date],
                       closeDate: Option[Date], balance: Balance): \/[NonEmptyList[String], Account] = {

      val od = openDate.getOrElse(today)

      (
        validateAccountNo(no) |@|
          validateOpenCloseDate(openDate.getOrElse(today), closeDate) |@|
          validateRate(rate)
        ) { (n, d, r) =>
        SavingsAccount(n, name, r, d._1, d._2, balance)
      }.disjunction
    }

    private def validateAccountAlreadyClosed(a: Account) = {
      if (a.dateOfClose isDefined) s"Account ${a.no} is already closed".failureNel[Account]
      else a.successNel[String]
    }

    private def validateCloseDate(a: Account, cd: Date) = {
      if (cd before a.dateOfOpen.get) s"Close date [$cd] cannot be earlier than open date [${a.dateOfOpen.get}]".failureNel[Date]
      else cd.successNel[String]
    }

    def close(a: Account, closeDate: Date): \/[NonEmptyList[String], Account] = {
      (validateAccountAlreadyClosed(a) |@| validateCloseDate(a, closeDate)) { (acc, d) =>
        acc match {
          case c: CheckingAccount => c.copy(dateOfClose = Some(closeDate))
          case s: SavingsAccount  => s.copy(dateOfClose = Some(closeDate))
        }
      }.disjunction
    }

    private def checkBalance(a: Account, amount: Amount) = {
      if (amount < 0 && a.balance.amount < -amount) s"Insufficient amount in ${a.no} to debit".failureNel[Account]
      else a.successNel[String]
    }

    def updateBalance(a: Account, amount: Amount): \/[NonEmptyList[String], Account] = {
      (validateAccountAlreadyClosed(a) |@| checkBalance(a, amount)) { (_, _) =>
        a match {
          case c: CheckingAccount => c.copy(balance = Balance(c.balance.amount + amount))
          case s: SavingsAccount  => s.copy(balance = Balance(s.balance.amount + amount))
        }
      }.disjunction
    }

    def rate(a: Account) = a match {
      case SavingsAccount(_, _, r, _, _, _) => r.some
      case _ => None
    }
  }



  trait Repository[A, IdType] {
    def query(id: IdType): Try[Option[A]]

    def store(a: A): Try[A]
  }

  trait AccountRepository {
    def query(no: String): \/[NonEmptyList[String], Option[Account]]
    def store(a: Account): \/[NonEmptyList[String], Account]
    def balance(no: String): \/[NonEmptyList[String], Balance] = query(no) match {
      case \/-(Some(a)) => a.balance.right
      case \/-(None) => NonEmptyList(s"No account exists with no $no").left[Balance]
      case a @ -\/(_) => a
    }
    def query(openedOn: Date): \/[NonEmptyList[String], Seq[Account]]
    def all: \/[NonEmptyList[String], Seq[Account]]
  }

}
