package com.mayo.planette.business_scripts.interpreters

import akka.actor.ActorSystem
import com.mayo.planette.abstraction.production.server.accounts.interpreter.AccountsServiceProduction
import com.mayo.planette.abstraction.terminology.DataDSL.DataStoreRequest

import scala.concurrent.ExecutionContext
import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/20/16.
 */
object AccountsServiceProduct extends AccountsServiceProduction {
  val system = ActorSystem("AccountsServiceProduct")
  override val dbDriver: ~>[DataStoreRequest, Id.Id] = AccountsRepositoryInMemInterpreter
  override implicit val ctxt: ExecutionContext = system.dispatcher
}

