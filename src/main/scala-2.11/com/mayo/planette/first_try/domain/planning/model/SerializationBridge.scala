package com.mayo.planette.first_try.domain.planning.model

import com.mayo.planette.first_try.domain.planning.model.abstract_dsl.PlanDSL
import com.mayo.planette.first_try.domain.wishlists.model.WishlistDSL

/**
 * @author yoav @since 6/27/16.
 */
trait SerializationBridge[A,B] {

  def toA(b:B): A
  def toB[BLike <: B](a:A): BLike
}
