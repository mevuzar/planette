package com.mayo.planette.business_scripts

import akka.actor.ActorSystem
import com.mayo.planette.abstraction.production.client.{AccountsUserInteractions, ClientAccountsProductionService}
import com.mayo.planette.abstraction.production.common.UserInteractionProductionInterpreter
import com.mayo.planette.abstraction.production.server.accounts.interpreter.AccountsServiceProduction
import com.mayo.planette.abstraction.terminology.DataDSL.DataStoreRequest
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperation
import com.mayo.planette.business_scripts.interpreters.{StdInInteractionInterpreter, AccountsRepositoryInMemInterpreter, DirectServerCommunication}
import com.mayo.planette.abstraction.terminology.client.Interaction

import scala.concurrent.ExecutionContext
import scalaz.{Free, Id, ~>}

/**
 * @author yoav @since 7/6/16.
 */
object SignUpScript extends App {
  val system = ActorSystem()
  implicit val ctxt = system.dispatcher

  val accountService: AccountsServiceProduction = new AccountsServiceProduction {
    override val dbDriver: ~>[DataStoreRequest, Id.Id] = AccountsRepositoryInMemInterpreter
    override implicit val ctxt: ExecutionContext = system.dispatcher
  }

  val userInteraction: AccountsUserInteractions = new AccountsUserInteractions{}


  val userAccounts: ClientAccountsProductionService = new ClientAccountsProductionService {

    override implicit val ctxt: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    override val interpreter: ~>[ServiceOperation, Id.Id] = new DirectServerCommunication(accountService)

    override def serialize[A, B]: (A) => B = ???

    override def deserialize[A, B]: (A) => B = ???


  }

  //val details = userInteraction.askSignUpDetails(StdInInteractionInterpreter)

  //println(details)

  val userInputToRequest = userAccounts.signUp.compose(userInteraction.askSignUpDetails)

  val signUpResult = userInputToRequest(StdInInteractionInterpreter)

  Thread.sleep(500)
  println(signUpResult)

}
