package com.mayo.planette.first_try

import com.mayo.planette.Monadicon

import scala.concurrent.Future
import scala.util.Try

/**
 * @author yoav @since 6/21/16.
 */
package object domain {

  case class FA[A](a:A) extends Monadicon[A] {
    override type F[A] = FA[A]

    override def get: A = a

    override def flatMap[U](f: (A) => FA[U]): FA[U] = f(a)

    override def map[U](f: (A) => U): FA[U] = FA(a.asInstanceOf[U])
  }

    trait Operations{
      //val op: OperationT
      type Operation[A, B] = A => B  //op.Func[A,B]
      type AuthenticationToken
      type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]
    }

  trait OperationT {
    type Result
    type Func[A,B] = A => Result
  }

//  object ServerOperations {
//
//    type Operation[A, B] = A => B
//    type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]
//    type AuthenticationToken = AnyRef
//  }

  //type Operation[A, B] = ServerOperations.Operation[A, B]
  //type AuthenticationToken = ServerOperations.AuthenticationToken
  //type AuthenticatedOperation[A, B] = ServerOperations.AuthenticatedOperation[A, B]

  trait Serialized[A]

  object Serialized {
    def apply[A](a: A): Serialized[A] = ???  //TODO: implement sreialization usin typeclass pattern
  }

  trait WithId[Id] {
    val id: Id
  }

}
