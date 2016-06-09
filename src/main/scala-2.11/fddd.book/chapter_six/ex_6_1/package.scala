package fddd.book.chapter_six

import scala.util.{Success, Try}
import scalaz.{EitherT, NonEmptyList, \/}
import scalaz._
import Scalaz._

/**
 * @author yoav @since 6/7/16.
 */
package object ex_6_1 {
  type Valid[A] = String \/ A
  type ValidList[A] = EitherT[List, String, A]

  def validate[A](a: Try[A]): Valid[A] = a match {
      case Success(result) => result.right
      case scala.util.Failure(e) => e.getMessage.left[A]
    }
  def validateAndList[A, B](cos: List[A])(ff: A => B): EitherT[List, String, B] = EitherT[List,String,B] {
      cos.map(m => validate(Try(ff(m))))
    }
}
