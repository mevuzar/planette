package com.mayo.planette

import scala.concurrent.Future

/**
 * @author yoav @since 6/21/16.
 */
package object domain {
  type ServerOperation[Request, Response] = Request => Future[Response]

  trait F[A] extends Monadic[A, F]

  //  trait ServerOperations{
  //
  //    type Operation[A, B] = A => Monadic[B, F]
  //    type AuthenticationToken
  //    type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]
  //  }

  object ServerOperations {
    //extends ServerOperations {
    type Operation[A, B] = A => Monadic[B, F]
    type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]
    //override type F[A] = this.type
    type AuthenticationToken = AnyRef
  }

  type Operation[A, B] = ServerOperations.Operation[A, B]
  //type AuthenticationToken = ServerOperations.AuthenticationToken
  type AuthenticatedOperation[A, B] = ServerOperations.AuthenticatedOperation[A, B]

  trait Serialized[A]

  object Serialized {
    def apply[A](a: A): Serialized[A] = ???  //TODO: implement sreialization usin typeclass pattern
  }

  trait WithId[Id] {
    val id: Id
  }

}
