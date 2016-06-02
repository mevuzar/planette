package fddd.book.chapter_three.injection

import scala.util.Try

/**
 * @author yoav @since 5/30/16.
 */
trait Repository[A, IdType] {
  def query(id: IdType): Try[Option[A]]

  def store(a: A): Try[A]
}
