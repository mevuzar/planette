package com.mayo.planette.abstraction.production.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpResponse, HttpEntity, HttpRequest, RequestEntity}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.mayo.planette.abstraction.terminology.client.ServerCommunicationMethod
import com.mayo.planette.abstraction.terminology.common.Serializer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author yoav @since 7/6/16.
 */
trait ServerCommunicationMethodProductionInterpreter extends ServerCommunicationMethod with Serializer with ServerHttpCallConfiguration {

  override type ResponseType = String
  override type ServerCaller[A] = A => Future[ResponseType]

  implicit lazy val system = ActorSystem()
  implicit lazy val materializer = ActorMaterializer()

  def callWithRequest(request: HttpRequest): Future[HttpResponse]

  def call[A](freeCall: FreeCall[Unit]): ServerCaller[A] = {a =>
    for {
        ent <- Future.successful(serialize[A,String](a))
        call <- callWithRequest(getRequestByCall(freeCall))
        response <- Unmarshal(call.entity).to[String]
      } yield response
  }


  def callMap[A, B](freeCall: FreeCall[Unit], f: ResponseType => B): A => Future[B] = {
    val result = futureMapper(f).compose(call[A](freeCall))
    result
  }

  def futureMapper[B](f: ResponseType => B): Future[ResponseType] => Future[B] = { future =>
    future.map(f)
  }
}

trait ServerHttpCallConfiguration {
  def getRequestByCall(freeCall: FreeCall[Unit]): HttpRequest
}