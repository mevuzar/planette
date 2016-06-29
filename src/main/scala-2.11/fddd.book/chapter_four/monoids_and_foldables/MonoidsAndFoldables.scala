package fddd.book.chapter_four.monoids_and_foldables

import fddd.book.chapter_four.{AUD, INR, USD, DR, Balance, Money, SyntacticShugas, Transaction}

/**
 * @author yoav @since 5/30/16.
 */
object MonoidsAndFoldables {

  trait Analytics[Transaction, Balance, Money] {
    def maxDebitOnDay(txns: List[Transaction])(implicit m: Monoid[Money]): Money
    def sumBalances(bs: List[Balance])(implicit m: Monoid[Money]): Money
  }

  object Analytics extends Analytics[Transaction, Balance, Money]{
    import SyntacticShugas.mapReduce

    override def maxDebitOnDay(txns: List[Transaction])(implicit m: Monoid[Money]): Money =
      mapReduce(txns.filter(_.transactionType == DR))(valueOfTransaction)

    override def sumBalances(bs: List[Balance])(implicit m: Monoid[Money]): Money =
      mapReduce(bs)(balanceToCredit)

    def valueOfTransaction(t: Transaction): Money = t.amount
    def balanceToCredit(b: Balance): Money = b.amount
  }

}

object MonoidsAndFoldablesApp extends App{
  val txns = List(Transaction(java.util.UUID.randomUUID.toString, Money(Map(USD -> 89))),
    Transaction(java.util.UUID.randomUUID.toString, Money(Map(INR -> 89))),
    Transaction(java.util.UUID.randomUUID.toString, Money(Map(AUD -> 89))))
   val maxDeb = MonoidsAndFoldables.Analytics.maxDebitOnDay(txns)
  println(s" maxDeb: $maxDeb")
}