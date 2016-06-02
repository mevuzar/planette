package fddd.book.chapter_four.trading

import scalaz.Kleisli

/**
 * @author yoav @since 6/2/16.
 */

trait ClientOrder
trait Order
trait Market
trait Execution
trait Trade
trait Account

object TradingServicePlain extends TradingServicePlain[ClientOrder, Order, Market, Execution, Trade, Account]{
  override def clientOrders: (ClientOrder) => List[Order] = ???

  override def execute(market: Market, broker: Account): (Order) => List[Execution] = ???

  override def allocate(accounts: List[Account]): (Execution) => List[Trade] = ???
}

object TradingServiceKleisli extends TradingServiceKleisli[ClientOrder, Order, Market, Execution, Trade, Account]{
  override def clientOrders: Kleisli[List, ClientOrder, Order] = ???

  override def execute(market: Market, broker: Account): Kleisli[List, Order, Execution] = ???

  override def allocate(accounts: List[Account]): Kleisli[List, Execution, Trade] = ???
}


trait TradingServicePlain[ClientOrder, Order, Market, Execution, Trade, Account] {
  def clientOrders: ClientOrder => List[Order]
  def execute(market: Market, broker: Account): Order => List[Execution]
  def allocate(accounts: List[Account]): Execution => List[Trade]
}

trait TradingServiceKleisli[ClientOrder, Order, Market, Execution, Trade, Account] {
  def clientOrders: Kleisli[List,ClientOrder,Order]
  def execute(market: Market, broker: Account): Kleisli[List,Order,Execution]
  def allocate(accounts: List[Account]): Kleisli[List,Execution,Trade]
}
