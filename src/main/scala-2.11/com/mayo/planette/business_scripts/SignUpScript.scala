package com.mayo.planette.business_scripts

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import com.mayo.planette.abstraction.production.client.{ClientAccountsProductionService, FreeCall, ServerCommunicationMethodProductionInterpreter}
import com.mayo.planette.abstraction.production.common.UserInteractionProductionInterpreter
import com.mayo.planette.abstraction.production.common.accounts.interpreter.AccountsServiceProduction
import com.mayo.planette.abstraction.production.common.accounts.model
import com.mayo.planette.abstraction.terminology.server.AccountsRepository
import com.mayo.planette.first_try.product.demo.common.serialization.JsonSerializer

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
 * @author yoav @since 7/6/16.
 */
object SignUpScript {
  val accountService: AccountsServiceProduction = ??? //new AccountsServiceProduction {
//
//    implicit val system = ActorSystem()
//
//    import model.AccountModel._
//
//    override implicit val ctxt: ExecutionContext = system.dispatcher
//    override val accountRepository: AccountsRepository[UserAccount, UUID] = ???
//    override type Credentials = AccountCredentials
//    override type SignUpRequest = UserSignupDetails
//    override type AuthenticationToken = UserToken
//    override type Account = UserAccount
//
//    override def signUpRequestToAccount: (UserSignupDetails) => Account = { request =>
//      val token = UUID.randomUUID
//      val id = UUID.randomUUID
//      val account = UserAccount.generateAccount(id, AccountCredentials(request.userName, request.password), request.userName, request.mail)
//      account.get
//    }
//
//    override def signUpRequestToAuthenticationToken: (UserSignupDetails) => UserToken = ???
//
//    val map = mutable.HashMap.empty[UUID, (UUID, Account)]
//
//
//    //override def signIn: (Credentials) => Future[Try[UserToken]]
//
//    override def signOut: (UserToken) => Future[Boolean] = { token =>
//      Future {
//        if (map.contains(token.accountId)) {
//          map.remove(token.accountId)
//          true
//        }
//        else {
//          false
//        }
//      }
//    }
//
//
//  }
  val userInteraction: UserInteractionProductionInterpreter = ???
  val userAccounts: ClientAccountsProductionService = ???

//  object ServerCommunicationMethodProductionInterpreter extends ServerCommunicationMethodProductionInterpreter with JsonSerializer {
//    override def callWithRequest(request: HttpRequest): Future[HttpResponse] = ???
//
//    override def getRequestByCall(freeCall: FreeCall[Unit]): HttpRequest = ???
//
//    override def deserialize[A, B]: (A) => B = a => deserialize[B](a.toString)
//
//    override def serialize[A, B]: (A) => B = { a => serialize(a) }
//  }

  //Http().singleRequest(getRequestByCall(freeCall).withEntity(ent))
  //compose user input and request to server - in order to concretize we should have a concrete AccountService
  // implementation and ServerHttpCallConfiguration
  val userInputToRequest = userAccounts.signUp.compose(userInteraction.getInfo[userAccounts.SignUpRequest])

}
