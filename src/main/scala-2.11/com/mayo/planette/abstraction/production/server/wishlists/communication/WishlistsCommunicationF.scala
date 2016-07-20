package com.mayo.planette.abstraction.production.server.wishlists.communication

import com.mayo.planette.abstraction.production.common.model.AccountModel.UserToken
import com.mayo.planette.abstraction.production.common.model.WishlistsModel.{Wishlist, Wish}
import com.mayo.planette.abstraction.terminology.FutureStringOr
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceMethodCall
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperations._
import scala.util.Try
import java.util.UUID
/**
 * @author yoav @since 7/19/16.
 */
object WishlistsCommunicationF {

  sealed trait WislistsMethodCall[+A] extends ServiceMethodCall[A]

  case class CreateWishlistCall(details: (UserToken, (UUID, List[Wish]))) extends WislistsMethodCall[FutureStringOr[Try[Wishlist]]]

  case class UpdateWishlistCall(details: (UserToken, (UUID, List[Wish], List[Wish]))) extends WislistsMethodCall[FutureStringOr[Try[Wishlist]]]

  case class DeleteWishlistCall(details: (UserToken, UUID)) extends WislistsMethodCall[FutureStringOr[Try[Unit]]]

  object WishlistsCommunicationOperations {
    def createWishlist(details: Tuple2[UserToken, (UUID, List[Wish])]) = serviceOperation(CreateWishlistCall(details))

    def updateWishlist(details: (UserToken, (UUID, List[Wish], List[Wish]))) = serviceOperation(UpdateWishlistCall(details))

    def deleteWishlist(details: (UserToken, UUID)) = serviceOperation(DeleteWishlistCall(details))
  }

}
