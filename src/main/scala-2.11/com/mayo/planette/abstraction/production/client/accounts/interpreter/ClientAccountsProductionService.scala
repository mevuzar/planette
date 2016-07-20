package com.mayo.planette.abstraction.production.client.accounts.interpreter

import java.util.UUID

import com.mayo.planette.abstraction.production.common.model.AccountModel.{AccountCredentials, UserAccount, UserSignupDetails, UserToken}
import com.mayo.planette.abstraction.production.server.accounts.communication.AccountsCommunicationF.AccountsCommunicationOperations
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperation
import com.mayo.planette.abstraction.terminology.client.ClientAccountService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Try}
import scalaz.{Free, Id, ~>}


/**
 * @author yoav @since 7/6/16.
 */
trait ClientAccountsProductionService extends ClientAccountService {
  //with Serializer {

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


  override def signUp: (SignUpRequest) => Future[Try[Account]] = { request => {
    val script = AccountsCommunicationOperations.signUp(request)
    val result = Free.runFC(script)(interpreter).right.get
    result onSuccess {
      case accountTry => accountTry.map(dataStore.storeAccount)
      case _ =>
    }
    result
  }
  }

  override def signIn: (SignInRequest) => Future[Try[AuthenticationToken]] = { request => {
    val script = AccountsCommunicationOperations.signIn(request)
    val futureTryToken = Free.runFC(script)(interpreter).right.get
    futureTryToken onSuccess {
      case Success(token) => dataStore.updateAccount(a => a.copy(token = token.token))
      case _ =>
    }

    futureTryToken
  }
  }

  override def signOut: (SignOutRequest) => Future[Boolean] = { request => {
    signOut(request)
  }

  }
}

