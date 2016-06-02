package fddd.book.chapter_four.monads.state_monad

import fddd.book.chapter_four.monoids_and_foldables.Monoid
import fddd.book.chapter_four.{AUD, Balance, INR, Money, Transaction, USD}

import scalaz.State
import scalaz.State._

/**
 * @author yoav @since 5/31/16.
 */
object StateMonad extends App with AccountService{

  val balances: Balances = Map(
    "a1" -> Balance(),
    "a2" -> Balance(Money(Map(AUD -> BigDecimal(200)))),
    "a3" -> Balance(),
    "a4" -> Balance(Money(Map(AUD -> BigDecimal(200)))),
    "a5" -> Balance()
  )

  val txns: List[Transaction] = List(
    Transaction("a1", Money(Map(USD -> BigDecimal(100)))),
    Transaction("a2", Money(Map(USD -> BigDecimal(100)))),
    Transaction("a1", Money(Map(INR -> BigDecimal(500000)))),
    Transaction("a3", Money(Map(USD -> BigDecimal(100)))),
    Transaction("a2", Money(Map(AUD -> BigDecimal(200))))
  )

  val updatedBalances =  updateBalance(txns) run balances
  println(updatedBalances._1.mkString("\n"))
}

trait AccountService {
  type AccountNo = String
  type Balances = Map[AccountNo, Balance]

  def updateBalance(txns: List[Transaction]): State[Balances, Unit] = {
    modify { b: Balances =>
      def transactionToBalances(a: Balances, txn: Transaction) =
        implicitly[Monoid[Balances]].op(a, Map(txn.txid -> Balance(txn.amount)))

      txns.foldLeft(b)(transactionToBalances)
    }
  }
}


