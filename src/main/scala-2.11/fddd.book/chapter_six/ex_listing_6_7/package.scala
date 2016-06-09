package fddd.book.chapter_six

import scala.concurrent.Future
import scalaz.Kleisli

/**
 * @author yoav @since 6/8/16.
 */
package object ex_listing_6_7 {
  type PFOperation = Kleisli[Future, Int, Seq[Int]]
}
