package com.mayo.planette.product.client
package domain

import com.mayo.planette.domain.wishlists.service.WishlistsService

/**
 * @author yoav @since 6/17/16.
 */
trait UserWishlistsService extends WishlistsService {

  def userFillQuestionaire: Operation[Unit, WishlistItemQuestionaire]

}
