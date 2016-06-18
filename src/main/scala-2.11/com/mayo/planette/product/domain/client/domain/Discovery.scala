package com.mayo.planette.product.domain.client
package domain

/**
 * @author yoav @since 6/17/16.
 */
trait Discovery {
  type PlanSerachRequest
  type Plan

  def searchPlan: ServerOperation[PlanSerachRequest, Plan]
}
