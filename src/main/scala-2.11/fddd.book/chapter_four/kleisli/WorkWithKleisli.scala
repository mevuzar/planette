package fddd.book.chapter_four.kleisli

/**
 * @author yoav @since 6/2/16.
 */
import scalaz.Kleisli._
import scalaz.Scalaz._
import scalaz.{Order => OrderZ, _}


object WorkWithKleisli extends App {
  def listIntToListString: List[Int] => List[String] = ints => ints.map(_.toString)

  def listStringToOptionStringThroughComputation(computation: String => Boolean): String => List[Option[String]] =
    str => List(Some(str))

  def optionStringToComputedInt(computation: Int => Int): Option[String] => List[Int] = op =>{
    op.map(i => Range(0, i.toInt).map(computation)).toList.flatten
  }


  val listIntToListStringKli = kleisli(listIntToListString)
  val stringToOptionFilteredKli = kleisli(listStringToOptionStringThroughComputation(s => s.toInt < 4))
  val optionStringToComputedIntKli = kleisli(optionStringToComputedInt(i => i*10))

  val composed = listIntToListStringKli andThen stringToOptionFilteredKli andThen optionStringToComputedIntKli
  val result = composed run(List(1, 2, 3, 4, 5))
  println(s"composed kleisli: $result")

}

//trait TradingInterpreter extends Trading[Account, Trade, ClientOrder, Order, Execution, Market] {
//
//  def clientOrders: Kleisli[List, List[ClientOrder], Order] = kleisli(fromClientOrders)
//
//  def execute(market: Market, brokerAccount: Account) = kleisli[List, Order, Execution] { order =>
//    order.items.map { item =>
//      Execution(brokerAccount, item.ins, "e-123", market, item.price, item.qty)
//    }
//  }
//
//  def allocate(accounts: List[Account]) = kleisli[List, Execution, Trade] { execution =>
//    val q = execution.quantity / accounts.size
//    accounts.map { account =>
//      makeTrade(account, execution.instrument, "t-123", execution.market, execution.unitPrice, q)
//    }
//  }
//}
//
//object TradingInterpreter extends TradingInterpreter
//
//object Example extends App {
//  val market = HongKong
//
//  val tradeGenerator = TradingInterpreter.tradeGeneration(HongKong, "jojo", List("jbabo", "nisso", "miko"))
//  val result = tradeGenerator run (List(Map("instrument" -> "juju/2/4-jaja/5/6", "no" -> "12345", "customer" -> "your mom!")))
//
//  println(result)
//
//
//}