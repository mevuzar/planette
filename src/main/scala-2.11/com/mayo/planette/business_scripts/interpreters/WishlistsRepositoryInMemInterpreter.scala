package com.mayo.planette.business_scripts.interpreters

import akka.actor.Status.Success
import com.mayo.planette.abstraction.production.common.model.WishlistsModel.Wishlist
import com.mayo.planette.abstraction.production.server.wishlists.dal.WishlistsRepositoryF.DSL.{WishlistQuery, StoreWishlist, DeleteWishlist}
import com.mayo.planette.abstraction.terminology.DataDSL.{DataOpteration, DataStoreRequest}

import scala.collection.mutable.HashMap
import scala.util.Try
import scalaz.{~>, Id}
import java.util.UUID
/**
 * @author yoav @since 7/20/16.
 */
object WishlistsRepositoryInMemInterpreter extends (DataStoreRequest ~> Id.Id) {
  val map = HashMap.empty[UUID, Wishlist]

  override def apply[A](fa: DataStoreRequest[A]): Id.Id[A] = fa match {
    case DataOpteration(operation) =>
      operation match {
        case WishlistQuery(id) => Right(Try{map.get(id).getOrElse({throw new Exception})})
        case StoreWishlist(wishlist) => Right(Try[Unit]{map.put(wishlist.wishlistId, wishlist).getOrElse({throw new Exception})})
        case DeleteWishlist(id) => Right(Try[Unit]{map.remove(id).getOrElse({throw new Exception})})
      }
  }

}

/*
* object AccountsRepositoryInMemInterpreter extends (DataStoreRequest ~> Id.Id) {

  import Id._

  val map = HashMap.empty[UUID, (UserToken, UserAccount)]

  def apply[A](in: DataStoreRequest[A]): Id[A] =
    in match {
      //case Pure(a) => a
      case DataOpteration(operation) =>
        operation match {
          case AccountQuery(userId) =>

            Right(Try {
              map.find(a => a._1 == userId).get._2._2
            })

          case StoreAccount(account) =>
            val tryIt = Try {
              val token = UUID.randomUUID //.toString
              map.put(account.id, (UserToken(account.id, token), account))
              ()
            }

            Right(tryIt)

          case DeleteAccount(userId) =>
            Right(Try {
              map.remove(userId)
              ()
            })
        }
    }
}
* */