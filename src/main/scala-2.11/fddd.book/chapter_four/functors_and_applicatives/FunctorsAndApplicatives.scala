package fddd.book.chapter_four

package functors_and_applicatives

import java.util.Date

import fddd.book.chapter_four.{Account, SyntacticShugas}
import fddd.book.chapter_three.injection.Balance

import scalaz.Validation

/**
 * @author yoav @since 5/31/16.
 */
object FunctorsAndApplicatives {
  trait AccountService{
    import SyntacticShugas._

    type V[A] = Validation[String, A]

    def validateAccountNo(no: String): V[String]
    def validateOpenCloseDate(openDate: Option[Date], closeDate: Option[Date]): V[(Date, Option[Date])]
    def validateRateOfInterest(rate: BigDecimal): V[BigDecimal]

    val va = new Applicative[V] {
      override def ap[A, B](fx: V[(A) => B])(fa: V[A]): V[B] = ???

      //apply executes effects first and returns the constructed object - eager
      override def apply3[V[_], A, B, C, D](va: V[A], vb: V[B], vc: V[C])(f: (A, B, C) => D): V[D] = ???

      override def unit[A](a: => A): V[A] = ???

      override def map[A, B](a: V[A])(f: (A) => B): V[B] = ???
    }

    def savingsAccount(no: String, name: String, rate: BigDecimal, openDate: Option[Date], closeDate: Option[Date], balance: fddd.book.chapter_four.Balance): V[Account] = {
      va.apply3[V, String, (Date, Option[Date]), BigDecimal, Account](validateAccountNo(no),
        validateOpenCloseDate(openDate, closeDate),
        validateRateOfInterest(rate))({(n, d, r) => SavingsAccount(n, name, r, Some(d._1), d._2, balance)})

    }

    def accountsOpenedBefore(dt: Date): Option[Account]
    def accountFor(no: String): Option[Account]
    def interestOn(account: Account): BigDecimal
    def close(account: Account): Account
    def calculateInterest(dt: Date): Option[BigDecimal] = fmap(accountsOpenedBefore(dt))(interestOn)
    def calculateInterest(no: String): Option[BigDecimal] = fmap(accountFor(no))(interestOn)
    def closeAccount(no: String): Option[Account] = fmap(accountFor(no))(close)
  }


}
