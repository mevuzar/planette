package com.mayo.planette.abstraction.production.server.wishlists.interpreter

import java.util.UUID

import com.mayo.planette.abstraction.production.common.model.AccountModel.UserToken
import com.mayo.planette.abstraction.production.common.model.WishlistsModel
import com.mayo.planette.abstraction.production.common.model.WishlistsModel.Wish
import com.mayo.planette.abstraction.production.server.wishlists.dal.WishlistsRepositoryF.WishlistsDataOperations._
import com.mayo.planette.abstraction.terminology.DataDSL.DataStoreRequest
import com.mayo.planette.abstraction.terminology.common.WishlistsService
import com.mayo.planette.abstraction.terminology.server.AuthenticationService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scalaz.{Free, Id, ~>}

/**
 * @author yoav @since 7/17/16.
 */
trait WishlistsServiceProduction extends WishlistsService {
  override type Operation[A, B] = A => Future[B]
  override type AuthenticationToken = UserToken
  override type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]
  override type WishlistCreate = Tuple2[WishlistId, List[Wish]]
  override type Wishlist = WishlistsModel.Wishlist
  override type WishlistId = UUID
  override type WishlistItem = Wish
  override type WishlistUpdate = (WishlistId, List[Wish], List[Wish])

  val dbDriver: (DataStoreRequest ~> Id.Id)
  val authenticationService: AuthenticationService
  implicit val ctxt: ExecutionContext

  //override def createWishlist: (UserToken) => ((UUID, List[Wish])) => Future[Try[WishlistsModel.Wishlist]] = ???

  override def createWishlist: (UserToken) => ((UUID, List[Wish])) => Future[Try[WishlistsModel.Wishlist]] = { token => {
    if(authenticationService.authenticate(token).isFailure) Future.failed(new Exception("authentication failed")) //TODO: make concrete exception
      t => {
        val wishlist = WishlistsModel.Wishlist.generateWishlist(t._1, t._2)
        val stored = storeWishlist(wishlist)

        Future(stored)
      }


  }

  }

  override def deleteWishlist: (UserToken) => (UUID) => Future[Try[Unit]] = { token => { id => {
    if(authenticationService.authenticate(token).isFailure) Future.failed(new Exception("authentication failed")) //TODO: make concrete exception
    Future {
      deleteWishlist(id)
    }
  }
  }
  }

  override def updateWishlist: (UserToken) => ((UUID, List[Wish], List[Wish])) => Future[Try[WishlistsModel.Wishlist]] = { token => { t => {
    if(authenticationService.authenticate(token).isFailure) Future.failed(new Exception("authentication failed")) //TODO: make concrete exception
    Future {
      val updated = for {
        wishlist <- queryWishlist(t._1)
        stored <- storeWishlist(wishlist.updateWishes(t._2, t._3))
      } yield stored

      updated
    }
  }
  }
  }

  def storeWishlist(wishlist: Wishlist): Try[Wishlist] = {
    val script = store(wishlist)
    val tryResult = Free.runFC(script)(dbDriver).right.get
    tryResult match {
      case Success(()) => Success(wishlist)
      case Failure(e) => Failure(e)
    }
  }

  def queryWishlist(wishlistId: WishlistId): Try[Wishlist] = {
    val theQuery = query(wishlistId)
    val dbResult = Free.runFC(theQuery)(dbDriver).right.get
    dbResult
  }

  def deleteWishlist(wishlistId: java.util.UUID): Try[Unit] = {
    val theQuery = delete(wishlistId)
    val dbResult = Free.runFC(theQuery)(dbDriver)
    dbResult.right.get
  }
}

