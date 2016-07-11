package com.mayo.planette.abstraction.production.client

import scalaz.{Functor, Free}

/**
 * @author yoav @since 7/6/16.
 */
trait MethodName

trait ServiceName

sealed trait ServerCallF[+A]

case class ServerCall[+A](serviceName: ServiceName, methodName: MethodName, next: A) extends ServerCallF[A]

object ServerCallF {
  implicit val functor: Functor[ServerCallF] = new Functor[ServerCallF] {
    override def map[A, B](fa: ServerCallF[A])(f: (A) => B): ServerCallF[B] = fa match {
      case ServerCall(serviceName, methodName, next) => ServerCall(serviceName, methodName, f(next))
    }
  }
}

trait ServiceCaller {

  import scalaz.Free.liftF

  def call(serviceName: ServiceName, methodName: MethodName): FreeCall[Unit] =
    liftF(ServerCall(serviceName, methodName, ()))
}