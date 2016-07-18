package com.mayo.planette.abstraction.production.server.wishlists

import com.mayo.planette.abstraction.production.common.model.{WishlistsModel, AccountModel}
import AccountModel.UserToken
import com.mayo.planette.abstraction.production.common.model.WishlistsModel.{Wish, Wishlist}
import com.mayo.planette.abstraction.terminology.common.WishlistsService
import java.util.UUID
import scala.concurrent.Future

/**
 * @author yoav @since 7/17/16.
 */
trait WishlistsServiceInterpreter extends WishlistsService{
  override type Operation[A, B] = A => Future[B]
  override type AuthenticationToken = UserToken
  override type AuthenticatedOperation[A,B] = AuthenticationToken => Operation[A,B]
  override type WishlistCreate = (WishlistId, List[Wish])
  override type Wishlist = WishlistsModel.Wishlist
  override type WishlistId = UUID
  override type WishlistItem = Wish
  override type WishlistUpdate = (WishlistId, List[Wish], List[Wish])

  override def createWishlist: (UserToken) => ((UUID, List[Wish])) => Future[WishlistsModel.Wishlist] = ???

  override def deleteWishlist: (UserToken) => (UUID) => Future[Boolean] = ???

  override def updateWishlist: (UserToken) => ((UUID, List[Wish], List[Wish])) => Future[WishlistsModel.Wishlist] = ???
}
