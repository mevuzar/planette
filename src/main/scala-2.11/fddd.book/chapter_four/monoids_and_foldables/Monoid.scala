package fddd.book.chapter_four.monoids_and_foldables

import fddd.book.chapter_four.{Balance, Currency, Money}

/**
 * @author yoav @since 5/30/16.
 */
trait Monoid[T] {
  def zero: T
  def op(t1: T, t2: T): T
}

  object Monoid {

    def apply[T](implicit monoid: Monoid[T]) = monoid

    implicit def MapMonoid[K, V: Monoid] = new Monoid[Map[K, V]] {
      def zero = Map.empty[K, V]
      def op(m1: Map[K, V], m2: Map[K, V]) = m2.foldLeft(m1) { (a, e) =>
        val (key, value) = e
        a.get(key).map(v => a + ((key, implicitly[Monoid[V]].op(v, value)))).getOrElse(a + ((key, value)))
      }
    }

    implicit val BigDecimalAdditionMonoid = new Monoid[BigDecimal] {
      val zero = BigDecimal(0)
      def op(i: BigDecimal, j: BigDecimal) = i + j
    }

    implicit def MoneyAdditionMonoid = new Monoid[Money] {
      val m = implicitly[Monoid[Map[Currency, BigDecimal]]]
      def zero = zeroMoney
      def op(m1: Money, m2: Money): Money =
        Money(m.op(m1.m, m2.m))
    }

    implicit def BalanceAdditionMonoid = new Monoid[Balance] {
      val m = implicitly[Monoid[Money]]
      def zero = Balance(zeroMoney)
      def op(b1: Balance, b2: Balance) =
        Balance(m.op(b1.amount, b2.amount))
    }

    final val zeroMoney: Money = Money(Monoid[Map[Currency, BigDecimal]].zero)
}