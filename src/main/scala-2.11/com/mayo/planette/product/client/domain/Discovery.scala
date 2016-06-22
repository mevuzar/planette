package com.mayo.planette.product.client
package domain

import com.mayo.planette.product.domain.client.ServerOp

/**
 * @author yoav @since 6/17/16.
 */
trait Discovery {
  type PlanSerachRequest
  type Plan

  def searchPlan: ServerOp[PlanSerachRequest, Plan]
}
