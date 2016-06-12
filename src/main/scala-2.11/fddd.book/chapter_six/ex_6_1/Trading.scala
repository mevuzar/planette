package fddd.book.chapter_six.ex_6_1

import scalaz.Kleisli.kleisli
import scalaz.Scalaz._
import scalaz.{Order => OrderZ, _}

/**
 * @author yoav @since 6/7/16.
 */

trait Trading[Account, Trade, ClientOrder, Order, Execution, Market] {


  type TradingOperation[A, B] = Kleisli[ValidList, A, B]

  def clientOrders: TradingOperation[List[ClientOrder], Order]

  def execute(market: Market, brokerAccount: Account): TradingOperation[Order, Execution]

  def allocate(accounts: List[Account]): TradingOperation[Execution, Trade]

  def tradeGeneration(market: Market, broker: Account, clientAccounts: List[Account]) = {
    val result = for {
      a <- clientOrders.right
      b <- execute(market, broker).right
      c <- allocate(clientAccounts).right[String]
    } yield c

    result
    clientOrders andThen execute(market, broker) andThen allocate(clientAccounts)
  }
}

object App extends App with ExecutionModel with OrderModel with RefModel with TradeModel {

  object Trading extends Trading[Account, Trade, ClientOrder, Order, Execution, Market] {
    def listToList: List[String] => List[String] = l => l

    val ll: Kleisli[List, List[String], String] = kleisli(listToList)

    override def clientOrders: Trading.TradingOperation[List[ClientOrder], Order] = kleisli[ValidList, List[ClientOrder], Order](fromClientOrders)

    override def execute(market: Market, brokerAccount: Account): Trading.TradingOperation[Order, Execution] = kleisli[ValidList, Order, Execution] { order =>
      validateAndList(order.items)(item => Execution(brokerAccount, item.ins, "e-123", market, item.price, item.qty))
      //validateAndList(order.items)(item => throw new Exception("WTF?!?!?!?!?!?!?"))
    }

    override def allocate(accounts: List[Account]): TradingOperation[Execution, Trade] = kleisli[ValidList, Execution, Trade] { execution =>
      val q = execution.quantity / accounts.size
      validateAndList(accounts)(account => makeTrade(account, execution.instrument, "t-123", execution.market, execution.unitPrice, q))
    }
  }

  val tradeGenerator = Trading.tradeGeneration(HongKong, "jojo", List("jbabo", "nisso", "miko"))
  val result = tradeGenerator.run(List(Map("instrument" -> "juju/2/4-jaja/5/6", "no" -> "12345", "customer" -> "your mom!")))
  println(result.getOrElse(null))

}

