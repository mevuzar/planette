package com.mayo.planette.domain.communication

import scala.concurrent.Future

/**
 * @author yoav @since 5/5/16.
 */

trait ClientHandlingService{
  type Request[TRequestObject]
  type Response[TResponseObject]
  type Chat

  def wrap[T](t: T): Response[T]
  def unwrap[T](request: Request[T]): T
  def handleRequest[T, R](request: Request[T])(handlingFunction: T => Future[Response[R]]): Future[Response[R]] = {
    handlingFunction(unwrap(request))
  }
  def openChat(request: Request[_]): Future[Chat]
}
