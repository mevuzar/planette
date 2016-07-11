package com.mayo.planette.abstraction.production.client

import com.mayo.planette.abstraction.terminology.common.{AccountsService, Serializer}

import scala.concurrent.Future
import scala.util.{Success, Try}

/**
 * @author yoav @since 7/6/16.
 */
trait ClientAccountsProductionService extends AccountsService with Serializer with AccountsServiceCaller{

  val serverCommunicationMethod: ServerCommunicationMethodProductionInterpreter
  type ResponseType = serverCommunicationMethod.ResponseType
  override type Operation[A, B] = A => Future[B]

  override def signUp: (SignUpRequest) => Future[AuthenticationToken] = {
    serverCommunicationMethod.
      callMap[SignUpRequest, AuthenticationToken](signUpCall, deserialize[ResponseType, AuthenticationToken])

  }

  override def signIn: (SignInRequest) => Future[Try[AuthenticationToken]] = {
    serverCommunicationMethod.
      callMap[SignInRequest, Try[AuthenticationToken]](signInCall, s=> {
      val d =deserialize[ResponseType, AuthenticationToken](s);Success(d)
    })
  }

  override def signOut: (SignOutRequest) => Future[Boolean] = {
    serverCommunicationMethod.
      callMap[SignOutRequest, Boolean](signOutCall, deserialize[ResponseType, Boolean])

  }
}

case object AccountServiceName extends ServiceName
case object SignUpMethod extends MethodName
case object SignInMethod extends MethodName
case object SignOutMethod extends MethodName

trait AccountsServiceCaller extends ServiceCaller{

  def signUpCall: FreeCall[Unit] =
    call(AccountServiceName, SignUpMethod)

  def signInCall: FreeCall[Unit] =
    call(AccountServiceName, SignInMethod)

  def signOutCall: FreeCall[Unit] =
    call(AccountServiceName, SignOutMethod)

}

object ShowInterpreter extends App with AccountsServiceCaller {

  def interpret[A](script: FreeCall[A], ls: List[String]): List[String] = script.fold(_ => ls, {
    case ServerCall(serviceName, methodName, next) =>
      interpret(next, ls ++ List(s"ServiceCall: $serviceName:$methodName"))
  })

  val calls = for {
    a <- signUpCall
    b <- signInCall
    c <- signOutCall
  } yield (a, b, c)

  val d = interpret(calls, List.empty[String])

  println(d)
}