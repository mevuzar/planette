package com.mayo.planette.domain.planning.model

import com.mayo.planette.domain.planning.model.abstract_dsl.PlanDSL
import com.mayo.planette.domain.wishlists.model.WishlistDSL

/**
 * @author yoav @since 6/27/16.
 */
trait SerializationBridge[A,B] {

  def toA(b:B): A
  def toB[BLike <: B](a:A): BLike
}
