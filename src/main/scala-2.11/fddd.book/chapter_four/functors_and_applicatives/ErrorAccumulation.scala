package fddd.book.chapter_four
package functors_and_applicatives

import java.util.Date

import scalaz.syntax.apply._
import scalaz.syntax.validation._

/**
 * @author yoav @since 6/1/16.
 */
object ErrorAccumulation extends AccountService with App {
  val validationWithMessedUpArguments = savingsAccount("111112222233333", "dd", -12, Some(new Date(2016, 8, 12)), Some(new Date(2016, 8, 11)), Balance())
  println(s"validationWithMessedUpArguments: $validationWithMessedUpArguments")

  val validationWithMessedUpDates = savingsAccount("111112223", "dd", 12, Some(new Date(2016, 8, 12)), Some(new Date(2016, 8, 11)), Balance())
  println(s"validationWithMessedUpDates: $validationWithMessedUpDates")

  val validationWithMessedUpRate = savingsAccount("11111222", "dd", -12, Some(new Date(2016, 8, 12)), Some(new Date(2016, 8, 13)), Balance())
  println(s"validationWithMessedUpRate: $validationWithMessedUpRate")

  val validationWithMessedUpNumber = savingsAccount("11111222fggggggggggg", "dd", 12, Some(new Date(2016, 8, 12)), Some(new Date(2016, 8, 13)), Balance())
  println(s"validationWithMessedUpNumber: $validationWithMessedUpNumber")

  val fineAccount = savingsAccount("1234", "dd", 12, Some(new Date(2016, 8, 12)), Some(new Date(2016, 8, 13)), Balance())
  println(s"fineAccount: $fineAccount")
}

trait AccountService {

  def savingsAccount(no: String,
                     name: String,
                     rate: BigDecimal,
                     openDate: Option[Date],
                     closeDate: Option[Date],
                     balance: fddd.book.chapter_four.Balance) = {
    val od = openDate.getOrElse(today)
    val validatedAccount = (
      validateAccountNo(no) |@|
        validateOpenCloseDate(Some(od), closeDate) |@|
        validateRateOfInterest(rate)
      ) { (n, d, r) =>
      SavingsAccount(n, name, r, Some(d._1), d._2, balance)
    }

    validatedAccount
  }

  def open(no: String,
           name: String,
           rate: BigDecimal,
           openDate: Option[Date],
           closeDate: Option[Date],
           balance: fddd.book.chapter_four.Balance) = {

  }

  private def validateAccountNo(no: String) = no.length match {
    case i if i <= 10 => no.successNel[String]
    case _ => "Account number should be composed from at most 10 characters.".failureNel[String]
  }

  private def validateOpenCloseDate(openDate: Option[Date], closeDate: Option[Date]) =
    if (openDate.map(_.after(closeDate.getOrElse(new Date))).getOrElse(false)) {
      "Open date should precede close date.".failureNel[(Date, Option[Date])]
    } else {
      (openDate.getOrElse(today), closeDate).successNel[String]
    }

  private def validateRateOfInterest(rate: BigDecimal) = {
    if (rate > 0) {
      rate.successNel[String]
    } else {
      "Rate cannot be negative.".failureNel[BigDecimal]
    }
  }

}
