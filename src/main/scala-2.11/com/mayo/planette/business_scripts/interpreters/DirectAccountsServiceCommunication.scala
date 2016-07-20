package com.mayo.planette.business_scripts.interpreters

import com.mayo.planette.abstraction.production.server.accounts.communication.AccountsCommunicationF.{AccountsMethodCall, SignInCall, SignOutCall, SignUpCall}
import com.mayo.planette.abstraction.production.server.accounts.interpreter.AccountsServiceProduction
import com.mayo.planette.abstraction.production.server.wishlists.communication.WishlistsCommunicationF._
import com.mayo.planette.abstraction.production.server.wishlists.interpreter.WishlistsServiceProduction
import com.mayo.planette.abstraction.terminology.ServiceDSL.{ServerCall, ServiceOperation}

import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/13/16.
 */
class DirectAccountsServiceCommunication(implicit accountsService: AccountsServiceProduction) extends (ServiceOperation ~> Id.Id) {


  override def apply[A](fa: ServiceOperation[A]): Id.Id[A] = fa match {
    case ServerCall(t: AccountsMethodCall[A]) => t match {
      case SignUpCall(details) => Right(accountsService.signUp(details))
      case SignInCall((id, credentials)) => Right(accountsService.signIn((id, credentials)))
      case SignOutCall(accountId) => Right(accountsService.signOut(accountId))
    }

  }
}

class DirectWishlistsServiceCommunication(implicit wishlistsService: WishlistsServiceProduction) extends (ServiceOperation ~> Id.Id) {
  override def apply[A](fa: ServiceOperation[A]): Id.Id[A] = fa match {
    case ServerCall(t: WislistsMethodCall[A]) => t match {
      case CreateWishlistCall(details) => Right(wishlistsService.createWishlist(details._1)(details._2))
      case UpdateWishlistCall(details) => Right(wishlistsService.updateWishlist(details._1)(details._2))
      case DeleteWishlistCall(details) => Right(wishlistsService.deleteWishlist(details._1)(details._2))
    }
  }
}