package com.mayo.planette.product.client
package domain

import com.mayo.planette.domain.wishlists.service.WishlistsService
import com.mayo.planette.domain.ServerOperations.Operation
/**
 * @author yoav @since 6/17/16.
 */
trait ClientWishlistsService extends WishlistsService {

  def userFillQuestionaire: Operation[Unit, listItemQuestionnaireService.wishListDSLImpl.AnsweredQuestionnaire]

}
