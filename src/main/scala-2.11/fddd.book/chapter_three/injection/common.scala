package fddd.book.chapter_three.injection

import java.util.Calendar

/**
 * @author yoav @since 5/30/16.
 */
object common {
  type Amount = BigDecimal

  val today = Calendar.getInstance.getTime
}
