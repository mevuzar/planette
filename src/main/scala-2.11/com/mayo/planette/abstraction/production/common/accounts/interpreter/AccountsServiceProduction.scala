package com.mayo.planette.abstraction.production.common.accounts
package interpreter

import com.mayo.planette.abstraction.terminology.common.AccountsService
import com.mayo.planette.abstraction.terminology.server.AccountsRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}


/**
 * @author yoav @since 7/6/16.
 */
trait AccountsServiceProduction extends AccountsService {
  override type Operation[A, B] = A => Future[B]

  val accountRepository: AccountsRepository[Account, AccountId]

  implicit val ctxt: ExecutionContext

  override def signUp: (SignUpRequest) => Future[AuthenticationToken] = { request =>
    Future {
      val account = signUpRequestToAccount(request)
      accountRepository.store(account)
      signUpRequestToAuthenticationToken(request)
    }
  }

  override def signIn: (SignInRequest) => Future[Try[AuthenticationToken]] = { signInRequest => {
    Future {
      val d = Try {
        val credentials = signInRequestToCredentials(signInRequest)
        val accountId = signInRequestToAccountId(signInRequest)
//        val updated = accountRepository.update(accountId, setAccountCredentials(credentials)).run
//        updated match{
//          case (Success(account), Success(())) => Success(accountToToken(account))
//          case _ => Failure(new Exception(""))//TODO: create typed exception
//        }

        credentialsToToken(credentials)
      }

      d
//        if (accountToCredentials(account) == credentials)
//          Success(credentialsToToken(credentials))
//        else
//          Failure(new Exception(s"Account with id: $accountId is not accessible with given credentials"))//TODO: create typed exception
//
//          match {
//          case acc =>
//            if (accountToCredentials(acc) == credentials)
//              Success(credentialsToToken(credentials))
//            else
//              Failure(new Exception(s"Account with id: $accountId is not accessible with given credentials"))//TODO: create typed exception
//
//
//          case _ => Failure(new Exception(s"Account with id: $accountId was not found")) //TODO: create typed exception
//        }
//      }


      }
    }
  }

  override def signOut: (SignOutRequest) => Future[Boolean] = { request =>
  Future{
//      val deleted = accountRepository.delete(signOutRequestToAccountId(request))
//      val d = deleted.run
      true
    }
  }
}
