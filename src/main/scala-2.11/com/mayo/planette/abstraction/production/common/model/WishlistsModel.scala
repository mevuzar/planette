package com.mayo.planette.abstraction.production.common.model

import java.util.UUID

import com.mayo.planette.abstraction.production.common.model.PlaningModel.WishType

/**
 * @author yoav @since 7/17/16.
 */
object WishlistsModel {
  case class Wishlist(wishlistId: UUID, accountId: UUID, wishes: List[Wish])
  case class Wish(wishId: UUID, wishType: WishType, planCategory: String)
}
