package fddd.book.chapter_three.injection

import java.util.Date
import common._
/**
 * @author yoav @since 5/30/16.
 */
case class Account(no: String, name: String, dateOfOpening: Date = today, dateOfClosing: Option[Date] = None,
                   balance: Balance = Balance(0))
