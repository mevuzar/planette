package com.mayo.planette.business_scripts.interpreters

import akka.actor.ActorSystem
import com.mayo.planette.abstraction.production.server.wishlists.interpreter.WishlistsServiceProduction
import com.mayo.planette.abstraction.terminology.DataDSL.DataStoreRequest
import com.mayo.planette.abstraction.terminology.server.AuthenticationService

import scala.concurrent.ExecutionContext
import scala.util.{Success, Try}
import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/20/16.
 */
object WishlistsServiceProduct extends WishlistsServiceProduction {
  val system = ActorSystem("WishlistsServiceProduct")
  override val dbDriver: ~>[DataStoreRequest, Id.Id] = WishlistsRepositoryInMemInterpreter
  override implicit val ctxt: ExecutionContext = system.dispatcher
  override val authenticationService: AuthenticationService = new AuthenticationService {
    override def authenticate[AuthenticationToken](token: AuthenticationToken): Try[Unit] = Success(())
  }
}
