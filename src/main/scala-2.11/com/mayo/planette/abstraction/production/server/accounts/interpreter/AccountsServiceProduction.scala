
package com.mayo.planette.abstraction.production.server.accounts.interpreter

import java.util.UUID

import com.mayo.planette.abstraction.production.common.accounts.model.AccountModel._
import com.mayo.planette.abstraction.production.server.accounts.dal.AccountRepositoryF.AccountDataOperations._
import com.mayo.planette.abstraction.terminology.DataDSL._
import com.mayo.planette.abstraction.terminology.common.AccountsService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scalaz.{Free, Id, ~>}


/**
 * @author yoav @since 7/6/16.
 */
trait AccountsServiceProduction extends AccountsService {
  override type Operation[A, B] = A => Future[B]
  override type Credentials = AccountCredentials
  override type SignUpRequest = UserSignupDetails
  override type AccountId = UUID
  override type SignOutRequest = AccountId
  override type SignInRequest = (UUID, AccountCredentials)
  override type AuthenticationToken = UserToken
  override type Account = UserAccount


  val dbDriver: (DataStoreRequest ~> Id.Id)

  implicit val ctxt: ExecutionContext

  override def signUp: (SignUpRequest) => Future[AuthenticationToken] = {

    request =>
      val future = Future {

        val tryAccount = UserAccount.generateAccount(AccountCredentials(request.userName, request.password), request.name, request.mail)
        val operation = for {
          account <- tryAccount
          stored <- storeAccount(account)
        } yield stored

        val result = operation match {
          case Success(account) => UserToken(account.id, account.token)
          case _ => throw new Exception("failed to store account") //TODO: make concrete exception
        }

        result
      }

      future
  }

  override def signIn: (SignInRequest) => Future[Try[AuthenticationToken]] = { signInRequest => {
    Future {
      val credentials = signInRequest._2
      val accountId = signInRequest._1
      val storedAccount = for {
        account <- queryAccount(accountId)
        stored <- storeAccount(UserAccount.revalidateToken(account))
      } yield stored

      storedAccount.map(a => UserToken(a.id, a.token))
    }
  }
  }

  override def signOut: (SignOutRequest) => Future[Boolean] = { request =>
    Future {
      deleteAccount(request).isSuccess
    }
  }

  def storeAccount(account: UserAccount): Try[UserAccount] = {
    val stored = store(account)
    val dbResult = Free.runFC(stored)(dbDriver).right.get
    dbResult match {
      case Success(()) => Success(account)
      case Failure(e) => Failure(e)
    }

  }

  def queryAccount(accountId: java.util.UUID): Try[UserAccount] = {
    val theQuery = query(accountId)
    val dbResult = Free.runFC(theQuery)(dbDriver).right.get
    dbResult
  }

  def deleteAccount(accountId: java.util.UUID): Try[Unit] = {
    val theQuery = delete(accountId)
    val dbResult = Free.runFC(theQuery)(dbDriver)
    dbResult.right.get
  }
}
