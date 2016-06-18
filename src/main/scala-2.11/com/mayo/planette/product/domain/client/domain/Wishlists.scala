package com.mayo.planette.product.domain.client
package domain

/**
 * @author yoav @since 6/17/16.
 */
trait Wishlists {
  type WishlistCreate
  type WishlistUpdate
  type Wishlist
  type WishlistId
  
  def createWishlist: ServerOperation[WishlistCreate, Wishlist]
  def deleteWishlist: ServerOperation[WishlistId, Boolean]
  def updateWishlist: ServerOperation[WishlistUpdate, Wishlist]
}
