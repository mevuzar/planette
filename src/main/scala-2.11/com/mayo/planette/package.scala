package com.mayo

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

}
