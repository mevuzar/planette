package com.mayo.planette.abstraction.production.server.wishlists

import java.util.UUID

import com.mayo.planette.abstraction.production.common.model.AccountModel.UserToken
import com.mayo.planette.abstraction.production.common.model.WishlistsModel
import com.mayo.planette.abstraction.production.common.model.WishlistsModel.Wish
import com.mayo.planette.abstraction.production.server.wishlists.dal.WishlistsRepositoryF.WishlistsDataOperations._
import com.mayo.planette.abstraction.terminology.DataDSL.DataStoreRequest
import com.mayo.planette.abstraction.terminology.common.WishlistsService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scalaz.{Free, Id, ~>}

/**
 * @author yoav @since 7/17/16.
 */
trait WishlistsServiceInterpreter extends WishlistsService {
  override type Operation[A, B] = A => Future[B]
  override type AuthenticationToken = UserToken
  override type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]
  override type WishlistCreate = Tuple2[WishlistId, List[Wish]]
  override type Wishlist = WishlistsModel.Wishlist
  override type WishlistId = UUID
  override type WishlistItem = Wish
  override type WishlistUpdate = (WishlistId, List[Wish], List[Wish])

  val dbDriver: (DataStoreRequest ~> Id.Id)


  implicit val ctxt: ExecutionContext

  //override def createWishlist: (UserToken) => ((UUID, List[Wish])) => Future[Try[WishlistsModel.Wishlist]] = ???

  override def createWishlist: (UserToken) => ((UUID, List[Wish])) => Future[Try[WishlistsModel.Wishlist]] = { token => { t => {
    val wishlist = WishlistsModel.Wishlist.generateWishlist(t._1, t._2)
    val stored = storeWishlist(wishlist)

    Future(stored)
  }
  }

  }

  override def deleteWishlist: (UserToken) => (UUID) => Future[Boolean] = ???

  override def updateWishlist: (UserToken) => ((UUID, List[Wish], List[Wish])) => Future[Try[WishlistsModel.Wishlist]] = ???

  def storeWishlist(wishlist: Wishlist): Try[Wishlist] = {
    val script = store(wishlist)
    val tryResult = Free.runFC(script)(dbDriver).right.get
    tryResult match {
      case Success(()) => Success(wishlist)
      case Failure(e) => Failure(e)
    }
  }
}

