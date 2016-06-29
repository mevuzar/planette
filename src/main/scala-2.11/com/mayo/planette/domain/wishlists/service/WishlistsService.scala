package com.mayo.planette.domain.wishlists.service

import com.mayo.planette.domain.WithId

/**
 * @author yoav @since 6/21/16.
 */
trait WishlistsService {
  type WishlistCreate <: WishlistMandatoryProperties
  type WishlistUpdate <: WishlistUpdateMandatoryProperties with WithId[WishlistId]
  type Wishlist <: WishlistMandatoryProperties with WithId[WishlistId]
  type WishlistId
  type AuthenticationToken
  type WishlistItem = listItemQuestionnaireService.wishListDSLImpl.AnsweredQuestionnaire


  trait WishlistMandatoryProperties {
    val items: List[WishlistItem]
  }

  trait WishlistUpdateMandatoryProperties {
    val itemsToAdd: List[WishlistItem]
    val itemsToRemove: List[WishlistItem]
  }

  type Operation[A, B]
  type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]

  val listItemQuestionnaireService: WishlistItemQuestionnaireService[AuthenticationToken]

  def createWishlist: AuthenticatedOperation[WishlistCreate, Wishlist]

  def deleteWishlist: AuthenticatedOperation[WishlistId, Boolean]

  def updateWishlist: AuthenticatedOperation[WishlistUpdate, Wishlist]

  //def createWishlistItem: AuthenticatedOperation[planQuestionnaireService.wishListDSLImpl.AnsweredQuestionnaire, WishlistItem]
}
