package fddd.book.chapter_six.ex_6_1

import java.util.{Calendar, Date}

import scala.util.Try
import scalaz.EitherT

trait OrderModel {
  this: RefModel =>

  case class LineItem(ins: Instrument, qty: BigDecimal, price: BigDecimal)

  case class Order(no: String, date: Date, customer: Customer, items: List[LineItem])

  type ClientOrder = Map[String, String]

  def fromClientOrders: List[ClientOrder] => ValidList[Order] = { cos =>
    validateAndList(cos){ co =>
        val ins = co("instrument").split("-")
        val lineItems = ins map { in =>
          val arr = in.split("/")
          LineItem(arr(0), BigDecimal(arr(1)), BigDecimal(arr(2)))
        }
        Order(co("no"), Calendar.getInstance.getTime, co("customer"), lineItems.toList)
    }
  }

}
