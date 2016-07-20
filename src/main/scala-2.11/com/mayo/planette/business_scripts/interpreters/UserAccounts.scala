package com.mayo.planette.business_scripts.interpreters

import akka.actor.ActorSystem
import com.mayo.planette.abstraction.production.client.accounts.interpreter.ClientAccountsProductionService
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperation

import scala.concurrent.ExecutionContext
import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/20/16.
 */
object UserAccounts extends ClientAccountsProductionService {

  implicit val accountsService = AccountsServiceProduct
  val system = ActorSystem("UserAccounts")
  override implicit val ctxt: ExecutionContext = system.dispatcher
  override val interpreter: ~>[ServiceOperation, Id.Id] = new DirectAccountsServiceCommunication

  override val dataStore = AccountLocalInMemRepository
}