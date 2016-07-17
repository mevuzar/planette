package com.mayo.planette.abstraction.production
package client

import com.mayo.planette.abstraction.production.common.accounts.model.AccountModel.{UserAccount, UserSignupDetails, AccountCredentials, UserToken}
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperation
import com.mayo.planette.abstraction.terminology.common.{AccountsService, Serializer}

import server.accounts.communication.AccountsCommunicationF.AccountsCommunicationOperations
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scalaz.{Free, Id, ~>}
import java.util.UUID


/**
 * @author yoav @since 7/6/16.
 */
trait ClientAccountsProductionService extends AccountsService with Serializer {

  implicit val ctxt: ExecutionContext

  val interpreter: (ServiceOperation ~> Id.Id)

  override type Operation[A, B] = A => Future[B]


  override type AccountId = UUID
  override type Credentials = AccountCredentials
  override type SignOutRequest = AccountId
  override type SignInRequest = (UUID, AccountCredentials)
  override type SignUpRequest = UserSignupDetails
  override type Account = UserAccount
  override type AuthenticationToken = UserToken

  override def signUp: (SignUpRequest) => Future[AuthenticationToken] = { request => {
      val script = AccountsCommunicationOperations.signUp(request)
      val result = Free.runFC(script)(interpreter).right.get

    result
  }
  }

  override def signIn: (SignInRequest) => Future[Try[AuthenticationToken]] = { request => {

      val script = AccountsCommunicationOperations.signIn(request)
      Free.runFC(script)(interpreter).right.get

  }
  }

  override def signOut: (SignOutRequest) => Future[Boolean] = { request => {
    signOut(request)
  }

  }
}

//case object AccountServiceName extends ServiceName
//case object SignUpMethod extends MethodName
//case object SignInMethod extends MethodName
//case object SignOutMethod extends MethodName
//
//trait AccountsServiceCaller extends ServiceCaller{
//
//  def signUpCall: FreeCall[Unit] =
//    call(AccountServiceName, SignUpMethod)
//
//  def signInCall: FreeCall[Unit] =
//    call(AccountServiceName, SignInMethod)
//
//  def signOutCall: FreeCall[Unit] =
//    call(AccountServiceName, SignOutMethod)
//
//}
//
//object ShowInterpreter extends App with AccountsServiceCaller {
//
//  def interpret[A](script: FreeCall[A], ls: List[String]): List[String] = script.fold(_ => ls, {
//    case ServerCall(serviceName, methodName, next) =>
//      interpret(next, ls ++ List(s"ServiceCall: $serviceName:$methodName"))
//  })
//
//  val calls = for {
//    a <- signUpCall
//    b <- signInCall
//    c <- signOutCall
//  } yield (a, b, c)
//
//  val d = interpret(calls, List.empty[String])
//
//  println(d)
//}