package fddd.book.chapter_four

import fddd.book.chapter_four.functors_and_applicatives.Functor
import fddd.book.chapter_four.monoids_and_foldables.{Monoid, Foldable}

/**
 * @author yoav @since 5/31/16.
 */
object SyntacticShugas {
  def mapReduce[F[_], A, B](as: F[A])(f: A => B)
                           (implicit fd: Foldable[F], m: Monoid[B]) = fd.foldMap(as)(f)

  def fmap[F[_], A, B](a: F[A])(f: A => B)(implicit ft: Functor[F]) = ft.map(a)(f)
}
