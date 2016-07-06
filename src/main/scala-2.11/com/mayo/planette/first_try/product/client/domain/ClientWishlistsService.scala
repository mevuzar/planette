package com.mayo.planette.first_try.product.client
package domain

import com.mayo.planette.first_try.domain.Operations
import com.mayo.planette.first_try.domain.wishlists.service.WishlistsService

/**
 * @author yoav @since 6/17/16.
 */
trait ClientWishlistsService extends WishlistsService {

  val operations: Operations
  import operations._

  def userFillQuestionaire: Operation[Unit, listItemQuestionnaireService.wishListDSLImpl.AnsweredQuestionnaire]

}
