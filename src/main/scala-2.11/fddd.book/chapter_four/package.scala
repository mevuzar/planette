package fddd.book

import java.util.{Calendar, Date}

import scala.util.{Failure, Success, Try}

/**
 * @author yoav @since 5/30/16.
 */
package object chapter_four {

  sealed trait TransactionType

  case object DR extends TransactionType

  case object CR extends TransactionType

  sealed trait Currency

  case object USD extends Currency

  case object AUD extends Currency

  case object JPY extends Currency

  case object INR extends Currency

  case class Money(m: Map[Currency, BigDecimal]) {
    def toBaseCurrency: BigDecimal = ???
  }

  case class Transaction(txid: String,
                         amount: Money,
                         accountNo: String = "",
                         date: Date = today,
                         transactionType: TransactionType = DR,
                         status: Boolean = false)

  case class Balance(amount: Money = Money(Map.empty[Currency, BigDecimal]))

  trait Account{
    val balance: Balance
  }

  def today = Calendar.getInstance.getTime

  case class SavingsAccount(no: String, name: String, rate: BigDecimal, openDate: Option[Date], closeDate: Option[Date], balance: Balance) extends Account

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
