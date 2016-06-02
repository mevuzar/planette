package fddd.book.chapter_four.kleisli


import scalaz._
import Scalaz._

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

object TradingServiceKleisli extends TradingServiceKleisli[Map[String, String], Order, Market, Execution, Trade, String]{
  override def clientOrders: Kleisli[List, Map[String, String], Order] = ???

  override def execute(market: Market, broker: String): Kleisli[List, Order, Execution] = ???

  override def allocate(accounts: List[String]): Kleisli[List, Execution, Trade] = ???

  case object HongKong extends Market

  val tradeGenerator = tradeGeneration(HongKong, "jojo", List("jbabo", "nisso", "miko"))
  val result = tradeGenerator run (Map("instrument" -> "juju/2/4-jaja/5/6", "no" -> "12345", "customer" -> "your mom!"))
  result

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

  def tradeGeneration(market: Market, broker: Account, clientAccounts: List[Account]) = {
    clientOrders               andThen
      execute(market, broker)    andThen
      allocate(clientAccounts)
  }
}

