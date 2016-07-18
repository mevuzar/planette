package com.mayo.planette.abstraction.production.server.wishlists.dal

import com.mayo.planette.abstraction.production.common.model.WishlistsModel._
import com.mayo.planette.abstraction.terminology.DataDSL.{DataCall, DataOperations}
import com.mayo.planette.abstraction.terminology.StringOr

/**
 * @author yoav @since 7/18/16.
 */
object WishlistsRepositoryF {
  import java.util.UUID

  import scala.util.Try
  import scalaz.Free

  /**
   * @author yoav @since 7/10/16.
   */

  object DSL {

    type FreeCall[A] = Free[DataCall, A]
    sealed trait WishlistDataCall[+A] extends DataCall[A]
    final case class WishlistQuery(userId: UUID) extends WishlistDataCall[StringOr[Try[Wishlist]]]

    final case class StoreWishlist(wishlist: Wishlist) extends WishlistDataCall[StringOr[Try[Unit]]]

    final case class DeleteWishlist(userId: UUID) extends WishlistDataCall[StringOr[Try[Unit]]]

  }

  object WishlistsDataOperations extends DataOperations{
    import DSL._

    def query(id: UUID) = dataOperation(WishlistQuery(id))

    def store[A](wishlist: Wishlist) = dataOperation(StoreWishlist(wishlist))

    def delete[A](id: UUID) = dataOperation(DeleteWishlist(id))

  }
}
