package fddd.book
package chapter_two

import java.time.{ZonedDateTime, Duration}




import scala.util.{Success, Try}

/**
 * @author yoav @since 3/7/16.
 */
object ChapterTwo extends App with AccountService {

  val s1 = SavingsAccount("dg", "sb001", 0.25)
  val s2 = SavingsAccount("rs", "sb002", 0.5)
  val s3 = SavingsAccount("ty", "sb003", 0.75)

  val res1 = List(s1, s2, s3).map(calculateInterest(_, Duration.ofSeconds(6)))

  println(res1)

  val res2 = List(s1, s2, s3).map(calculateInterest(_, Duration.ofSeconds(6))).
    foldLeft(BigDecimal(0))((a,e)=> e.map(_ + a).getOrElse(a))

  println(res2)

  val fut = for {
    s <- getAccountFrom("a1")
    b <- getCurrencyBalance(s)
    v <- calculateNetAssetValue(s, b) if (v > 100000)
  } yield (s, v)

  println(fut)

  def getCurrencyBalance(a: ConcreteAccount): Try[Amount] = Try(60)

  def getAccountFrom(no: String): Try[ConcreteAccount] = Try(ConcreteAccount(no, "Moshe", ZonedDateTime.now, Balance(88)))

  def calculateNetAssetValue(a: ConcreteAccount, balance: Amount): Try[Amount] = {
    val result = Try{BigDecimal(100001)}
    result
  }
  case class ConcreteAccount(no: String, name: String, dateOfOpening: ZonedDateTime, balance: Balance = Balance()) extends Account

}

trait Account {
  def no: String
  def name: String
  def dateOfOpening: ZonedDateTime
  def balance: Balance
}

case class CheckingAccount(theNumber: String, theName: String) extends Account {
  override def dateOfOpening: ZonedDateTime = ???

  override def no: String = theNumber

  override def name: String = theName

  override def balance: Balance = ???
}

trait InterestBearingAccount extends Account {
  def rateOfInterest: BigDecimal
}

case class SavingsAccount(theNumber: String, theName: String, theRateOfInterest: BigDecimal) extends InterestBearingAccount {
  override def rateOfInterest: BigDecimal = theRateOfInterest

  override def no: String = theNumber

  override def name: String = theName

  override def dateOfOpening: ZonedDateTime = ???

  override def balance: Balance = ???
}

case class MoneyMarketAccount(theNumber: String, theName: String, theRateOfInterest: BigDecimal) extends InterestBearingAccount {
  override def rateOfInterest: BigDecimal = theRateOfInterest

  override def no: String = theNumber

  override def name: String = theName

  override def dateOfOpening: ZonedDateTime = ???

  override def balance: Balance = ???
}

trait AccountService {
  import java.time.Duration
  def calculateInterest[A <: InterestBearingAccount](account: A, period: Duration): Try[BigDecimal] = {
    Success(account.rateOfInterest)
  }
}

object ModulesExample{

  object Modules {

    sealed trait TaxType

    case object Tax extends TaxType

    case object Fee extends TaxType

    case object Commission extends TaxType

    sealed trait TransactionType

    case object InterestComputation extends TransactionType

    case object Dividend extends TransactionType

    type Amount = BigDecimal

    case class Balance(amount: Amount = 0)

    trait TaxCalculationTable {
      type T <: TransactionType
      val transactionType: T

      def getTaxRates: Map[TaxType, Amount] = ???
    }

    trait TaxCalculation {
      type S <: TaxCalculationTable
      val table: S

      def calculate(taxOn: Amount): Amount =
        table.getTaxRates map {
          case (t, r) => doCompute(taxOn, r)
        } sum

      protected def doCompute(taxOn: Amount, rate: Amount): Amount = taxOn * rate
    }

    trait SingaporeTaxCalculation extends TaxCalculation {
      def calculateGST(tax: Amount, gstRate: Amount) = tax * gstRate
    }

    trait InterestCalculation {
      type C <: TaxCalculation
      val taxCalculation: C
    }

  }

  object ConcreteImplementation{
    import Modules._
    object InterestTaxCalculationTable extends TaxCalculationTable{
      type T = TransactionType
      val transactionType = InterestComputation
    }

    object TaxCalculation extends TaxCalculation{
      type S = TaxCalculationTable
      val table = InterestTaxCalculationTable
    }

    object InterestCalculation extends InterestCalculation{
      type C = TaxCalculation
      val taxCalculation = TaxCalculation
    }
  }
}