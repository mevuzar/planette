package com.mayo.planette.business_scripts

import akka.actor.ActorSystem
import com.mayo.planette.abstraction.production.client.wishlists.{WishlistsUserInteractions, ClientWishlistsProduction}
import com.mayo.planette.abstraction.production.common.model.AccountModel.{UserAccount, UserToken}
import com.mayo.planette.abstraction.production.common.model.WishlistsModel.Wish
import com.mayo.planette.abstraction.production.server.wishlists.interpreter.WishlistsServiceProduction
import com.mayo.planette.abstraction.terminology.DataDSL.DataStoreRequest
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperation
import com.mayo.planette.abstraction.terminology.client.PreDefInteractions.BooleanInteraction
import com.mayo.planette.abstraction.terminology.client.{AccountLocalRepository, Question, UserInteraction}
import com.mayo.planette.abstraction.terminology.server.AuthenticationService
import com.mayo.planette.business_scripts.interpreters.{WishlistsServiceProduct, ClientWishlistsProduct, WishlistsRepositoryInMemInterpreter, DirectWishlistsServiceCommunication, StdInInteractionInterpreter, AccountsRepositoryInMemInterpreter}

import java.util.UUID
import scala.collection.mutable.HashMap
import scala.concurrent.{Future, Await, ExecutionContext, duration}
import duration._
import scala.util.{Success, Try}
import scalaz.{Reader, Id, ~>}

/**
 * @author yoav @since 7/17/16.
 */
object WishlistsScript extends App {

  implicit lazy val wishlistsService = WishlistsServiceProduct
  object BasicUserInteraction extends UserInteraction
  lazy val userWishlists = ClientWishlistsProduct
  
  object WishlistsUserInteractions extends WishlistsUserInteractions

  def makeWishes(wishes: List[Wish] = List.empty[Wish]): (Question ~> Id.Id) => List[Wish] = {
    interpreter =>
    if(BasicUserInteraction.askUser(BooleanInteraction("do you wanna make a wish?"))(interpreter)){
      makeWishes(wishes ::: List(WishlistsUserInteractions.askForWish(interpreter)))(interpreter)
    }else {
      wishes
    }
  }

  def createWishlist(environment: Environment, interpreter: ~>[Question, Id.Id]) = {
    def t2Ta(userToken: UserToken): (UUID, List[Wish]) = {
      val wishes = makeWishes()(interpreter)
      (userToken.accountId, wishes)
    }
    for{
    token <- environment.getToken
    }yield userWishlists.createWishlist(token).compose(t2Ta)(token)

  }

}
