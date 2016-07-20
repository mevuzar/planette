package com.mayo.planette.business_scripts.interpreters

import com.mayo.planette.abstraction.production.client.wishlists.ClientWishlistsProduction
import com.mayo.planette.abstraction.production.server.wishlists.interpreter.WishlistsServiceProduction
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperation

import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/20/16.
 */
object ClientWishlistsProduct extends ClientWishlistsProduction {
  implicit val wishlistsProduction: WishlistsServiceProduction = WishlistsServiceProduct
  override val interpreter: ~>[ServiceOperation, Id.Id] = new DirectWishlistsServiceCommunication
}
