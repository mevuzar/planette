package fddd.book.chapter_four.monoids_and_foldables

/**
 * @author yoav @since 5/30/16.
 */
trait Foldable[F[_]] {
  def foldl[A, B](as: F[A], z: B, f: (B, A) => B): B
  def foldMap[A, B](as: F[A])(f: A => B)(implicit m: Monoid[B]): B = foldl(as, m.zero, (b: B, a: A) => {
    m.op(b, f(a))
  })
}

object Foldable {
  implicit val listFoldable = new Foldable[List] {
    def foldl[A, B](as: List[A], z: B, f: (B, A) => B) = as.foldLeft(z)(f)
  }
}
