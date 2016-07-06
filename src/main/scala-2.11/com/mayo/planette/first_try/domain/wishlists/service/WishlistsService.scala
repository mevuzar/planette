package com.mayo.planette.first_try.domain
package wishlists.service

/**
 * @author yoav @since 6/21/16.
 */
trait WishlistsService {

  val operations: Operations
  import operations._

  type WishlistCreate <: WishlistMandatoryProperties
  type WishlistUpdate <: WishlistUpdateMandatoryProperties with WithId[WishlistId]
  type Wishlist <: WishlistMandatoryProperties with WithId[WishlistId]
  type WishlistId
  //type AuthenticationToken
  type WishlistItem = listItemQuestionnaireService.wishListDSLImpl.AnsweredQuestionnaire


  trait WishlistMandatoryProperties {
    val items: List[WishlistItem]
  }

  trait WishlistUpdateMandatoryProperties {
    val itemsToAdd: List[WishlistItem]
    val itemsToRemove: List[WishlistItem]
  }

  //  type Operation[A, B]
  //  type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]

  val listItemQuestionnaireService: WishlistItemQuestionnaireService

  def createWishlist: AuthenticatedOperation[WishlistCreate, Wishlist]

  def deleteWishlist: AuthenticatedOperation[WishlistId, Boolean]

  def updateWishlist: AuthenticatedOperation[WishlistUpdate, Wishlist]

  //def createWishlistItem: AuthenticatedOperation[planQuestionnaireService.wishListDSLImpl.AnsweredQuestionnaire, WishlistItem]
}
