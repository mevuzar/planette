package com.mayo

import scala.util.Try

/**
 * @author yoav @since 6/21/16.
 */
package object planette {

  case class Reader[R, A](run: R => A) {
    def map[B](f: A => B): Reader[R, B] = Reader(r => f(run(r)))

    def flatMap[B](f: A => Reader[R, B]): Reader[R, B] = {
      Reader(r => f(run(r)).run(r))
    }
  }

  trait Applicable[T[A] <: Monadic[_, T]] extends Tryable[T]{
    def apply[A](r: => A): T[A]
  }

  trait Tryable[T[A] <: Monadic[_, T]]{
    def apply[_](r: => T[_]): Try[_] = Try(r.get)
  }


  trait Monadic[A, F[A]]{
    def get: A
    def map[U](f: A => U): F[U]
    def flatMap[U](f: A => F[U]): F[U]
  }

}
