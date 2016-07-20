package com.mayo.planette.abstraction.production.client.wishlists

import java.util.UUID

import com.mayo.planette.abstraction.production.common.model.AccountModel.UserToken
import com.mayo.planette.abstraction.production.common.model.WishlistsModel
import com.mayo.planette.abstraction.production.common.model.WishlistsModel.Wish
import com.mayo.planette.abstraction.production.server.wishlists.communication.WishlistsCommunicationF.WishlistsCommunicationOperations
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperation
import com.mayo.planette.abstraction.terminology.common.WishlistsService

import scala.concurrent.Future
import scala.util.Try
import scalaz.{Free, Id, ~>}

/**
 * @author yoav @since 7/19/16.
 */
trait ClientWishlistsProduction extends WishlistsService {
  override type Operation[A, B] = A => Future[B]
  override type AuthenticationToken = UserToken
  override type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]
  override type WishlistCreate = Tuple2[WishlistId, List[Wish]]
  override type Wishlist = WishlistsModel.Wishlist
  override type WishlistId = UUID
  override type WishlistItem = Wish
  override type WishlistUpdate = (WishlistId, List[Wish], List[Wish])

  val interpreter: (ServiceOperation ~> Id.Id)

  override def createWishlist: (UserToken) => ((UUID, List[Wish])) => Future[Try[WishlistsModel.Wishlist]] = { token => { t => {
    val script = WishlistsCommunicationOperations.createWishlist(token, t)
    val result = Free.runFC(script)(interpreter).right.get

    result
  }
  }
  }

  override def updateWishlist: (UserToken) => ((UUID, List[Wish], List[Wish])) => Future[Try[WishlistsModel.Wishlist]] = { token => { t => {
    val script = WishlistsCommunicationOperations.updateWishlist(token, t)
    val result = Free.runFC(script)(interpreter).right.get

    result
  }
  }
  }

  override def deleteWishlist: (UserToken) => (UUID) => Future[Try[Unit]] = { token => { t => {
    val script = WishlistsCommunicationOperations.deleteWishlist(token, t)
    val result = Free.runFC(script)(interpreter).right.get

    result
  }
  }
  }
}
