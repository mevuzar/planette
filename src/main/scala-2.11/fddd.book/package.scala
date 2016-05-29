package fddd

import java.util.Calendar

/**
 * Created by yoav on 1/13/16.
 */
package object book {
  type Amount = BigDecimal
  case class Balance(amount: Amount = 0)
  def today = Calendar.getInstance.getTime
}
