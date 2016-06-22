package com.mayo.planette.domain.wishlists.service

import com.mayo.planette.domain.WithId
import com.mayo.planette.domain.planning.abstract_dsl.PlanDSL

/**
 * @author yoav @since 6/21/16.
 */
trait Wishlists extends PlanDSL{
  type WishlistCreate
  type WishlistUpdate <: WishlistUpdateMandatoryProperties with WithId[WishlistId]
  type Wishlist <: WishlistMandatoryProperties with WithId[WishlistId]
  type WishlistId
  type AuthenticationToken
  type WishlistItem <: Plan
  type WishlistItemQuestionaire

  trait WishlistUpdateMandatoryProperties{
    val itemsToAdd: List[WishlistItem]
    val itemsToRemove: List[WishlistItem]
  }
  trait WishlistMandatoryProperties{
    val items: List[WishlistItem]
  }

  type Operation[A,B]
  type AuthenticatedOperation[A,B] = AuthenticationToken => Operation[A,B]

  def createWishlist: AuthenticatedOperation[WishlistCreate, Wishlist]
  def deleteWishlist: AuthenticatedOperation[WishlistId, Boolean]
  def updateWishlist: AuthenticatedOperation[WishlistUpdate, Wishlist]

  def createWishlistItem: AuthenticatedOperation[WishlistItemQuestionaire, WishlistItem]
}
