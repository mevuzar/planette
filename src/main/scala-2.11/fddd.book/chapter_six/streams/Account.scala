package fddd.book.chapter_six.streams

import java.util.{Calendar, Date}

object common {
  type Amount = BigDecimal

  def today = Calendar.getInstance.getTime
}

import fddd.book.chapter_six.streams.common._

case class Balance(amount: Amount = 0)

case class Account (no: String, name: String, dateOfOpen: Option[Date], dateOfClose: Option[Date] = None, 
  balance: Balance = Balance()) 
