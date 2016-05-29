package fddd.book
package chapter_one

import java.time.ZonedDateTime

/**
 * Created by yoav on 1/13/16.
 */
case class Account(no: String, name: String, dateOfOpening: ZonedDateTime, balance: Balance = Balance())
