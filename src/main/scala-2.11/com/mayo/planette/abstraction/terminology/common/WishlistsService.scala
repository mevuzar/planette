package com.mayo.planette.abstraction.terminology.common

import com.mayo.planette.abstraction.terminology.{AuthenticatedOperations, CommonTerms, CommonOperations}

/**
 * @author yoav @since 6/21/16.
 */
private[abstraction] trait WishlistsService extends CommonOperations with AuthenticatedOperations with CommonTerms{

  type WishlistCreate
  type WishlistUpdate
  type Wishlist
  type WishlistId
  type WishlistItem

  val listItemQuestionnaireService: WishlistItemQuestionnaireService

  def createWishlist: AuthenticatedOperation[WishlistCreate, Wishlist]

  def deleteWishlist: AuthenticatedOperation[WishlistId, Boolean]

  def updateWishlist: AuthenticatedOperation[WishlistUpdate, Wishlist]
}
