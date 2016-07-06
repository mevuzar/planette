package com.mayo.planette.first_try.product.demo

/**
 * @author yoav @since 7/4/16.
 */
package object cli_client {
  //  trait TryF[A] extends F[A] {
  //    override val value: A
  //
  //    override def flatMap[U](f: (A) => F[U]): F[U] = f(value)
  //
  //    override def get: A = value
  //
  //    override def map[U](f: (A) => U): F[U] = TryF(f(value))
  //  }

  //  object TryF {
  //    def apply[A](a: A) = new TryF[A]{
  //      override val value: A = a
  //    }
  //  }

  //  trait MonadicTry[A] extends Monadicon[A] {
  //    override type F[A] = Try[A] with Mono[A]
  //  }
  //
  //  object MonadicTry {
  //    def apply[A](a: A): MonadicTry[A] = new MonadicTry[A] {
  //      override def get: A = a
  //
  //      override def flatMap[U](f: (A) => Try[U]): Try[U] = Try(a).flatMap(f)
  //
  //      override def map[U](f: (A) => U): Try[U] = Try(a).map(f)
  //    }
  //  }


}
