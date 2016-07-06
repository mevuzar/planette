package fddd.book
package chapter_three

import java.util.Date

import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Properties}

import scala.util.{Success, Try}

/**
 * @author yoav @since 5/16/16.
 */
object FinalAlgebraOfTypes {


  def monads3Rules = {

    /*“A monad abstraction consists of the following three elements:
 
A) A type constructor M[A] expressed in Scala as trait M[A] or case class M[A] or class M[A]
B) A unit method that lifts a computation into the monad. In Scala we use the invocation of the class constructor for this purpose
C) A bind method, which sequences computations. In Scala flatMap is the bind
 
This implies that a monad is an algebraic structure. Any monad that has the above 3 elements also needs to honor the following 3 laws:
 
A) Identity. For a monad m, m flatMap unit => m
B) Unit. For a monad m, unit(v) flatMap f => f(v)
C) Associativity. For a monad m, m flatMap g flatMap h => m flatMap {x => g(x) flatMap h}”

Excerpt From: Debasish Ghosh. “Functional and Reactive Domain Modeling MEAP V12.” iBooks. https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewBook?id=41DC7B373407F08DC4B364BDDE80151E*/
    Try(1).flatMap(i => Try(i * 2).flatMap(j => Try(j * 3)))
  }

  trait AccountService {
    trait AccountMandatoryProperties{
      val balance: Balance
    }
    type Account <: AccountMandatoryProperties
    type Amount
    type Balance
    def open(no: String, name: String, openDate: Option[Date]): Try[Account]

    def close(account: Account, closeDate: Option[Date]): Try[Account]

    def debit(account: Account, amount: Amount): Try[Account]

    def credit(account: Account, amount: Amount): Try[Account]

    def balance(account: Account): Try[Balance]

    def transfer(from: Account, to: Account, amount: Amount): Try[(Account, Account, Amount)] = for {
      a <- debit(from, amount)
      b <- credit(to, amount)
    } yield (a, b, amount)


    def accountGen: Account
    def amountGen: Amount

    object DomainRules extends Properties("String") {
      implicit val arbitAccount = Arbitrary(Gen.const(accountGen))
      implicit val arbitAmount = Arbitrary(Gen.const(amountGen))
      property("Equal credit & debit in sequence retain the same balance") =
        forAll{(a: Account, m: Amount) => {
          val Success((before, after)) = for {
            b <- balance(a)
            c <- credit(a, m)
            d <- debit(c, m)
          } yield (b, d.balance)

          before == after
        }}

    }
  }



}
