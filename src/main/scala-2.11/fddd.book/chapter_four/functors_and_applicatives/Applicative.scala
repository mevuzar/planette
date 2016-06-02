package fddd.book.chapter_four.functors_and_applicatives

/**
 * @author yoav @since 5/31/16.
 */
trait Applicative[F[_]] extends Functor[F]{
  def ap[A,B](fx: F[A => B])(fa: F[A]): F[B]
  def unit[A](a: => A): F[A]
  def map2[A,B,C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = {

    val curriedF = f.curried //Function2[A,B,C] turns into the curried function A => B => C
    val curriedReturnTypeGivenTheArgument = map(fa)(curriedF) // map signature: map[A,B](a: F[A])(f: A => B): F[B]
    //The value type of curriedReturnTypeGivenTheArgument is F[B => C] because the curried function return type is B => C (given A)
    ap(curriedReturnTypeGivenTheArgument)(fb)
  }

  //apply executes effects first and returns the constructed object - eager
  def apply3[V[_],A,B,C,D](va: V[A], vb: V[B], vc: V[C])(f: (A,B,C) => D): V[D]

  //lift returns a curried function (which arguments are effects) that already
  // contains the method to create the object object - lazy
  def lift3[V[_], A,B,C,D](f: (A,B,C) => D): (V[A], V[B], V[C]) => V[D] = apply3(_,_,_)(f)
}
