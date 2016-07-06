package com.mayo.planette.first_try.product.domain

import akka.actor.Actor
import com.mayo.planette.Monadic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

/**
 * @author yoav @since 6/17/16.
 */
package object client {

  type ClientOperation[Request, Response] = Request => Future[Response]

  trait ServerConnection {
    type Address
    type Monada[A, B[_]] <: Monadic[A, B]
    type F[_]

    def call[A, _](address: Address, a: A): Try[_]
  }


  class ServerActor extends Actor {
    override def receive: Receive = {
      case any => any
    }
  }


  type AuthorizedServerOperation[Credentials, Request, Response] = CommunicationProtocol with Deserializer => Credentials => Request => Future[Response]
  type ServerOp[Request, Response] = CommunicationProtocol with Deserializer => Request => Future[Response]

  object ServerOperation {
    def apply[Request, Response]: ServerOp[Request, Response] = { protocol => {
      request => protocol.run(request).map(protocol.deserialize)
    }
    }
  }

  trait CommunicationProtocol {
    type Raw
    type RawResponse = Future[Raw]

    def run[Request](request: Request): RawResponse
  }

  trait IO[Raw] {
    self =>
    def run[Request](request: Request): Raw

    type RawResponse = Future[Raw]

    def map[B](f: Raw => B): IO[B] =
      new IO[B] {
        def run[Request](request: Request) = f(self.run(request))
      }

    def flatMap[B](f: Raw => IO[B]): IO[B] =
      new IO[B] {
        def run[Request](request: Request) = f(self.run(request)).run(request)
      }
  }

  trait Deserializer {
    def deserialize[Raw, T](raw: Raw): T
  }


  case class Reader[R, A](run: R => A) {
    def map[B](f: A => B): Reader[R, B] = Reader(r => f(run(r)))

    def flatMap[B](f: A => Reader[R, B]): Reader[R, B] = {
      Reader(r => f(run(r)).run(r))
    }
  }


}
