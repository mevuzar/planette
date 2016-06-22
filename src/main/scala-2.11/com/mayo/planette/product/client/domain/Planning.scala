package com.mayo.planette.product.client
package domain

import com.mayo.planette.product.domain.client.ServerOp

/**
 * @author yoav @since 6/17/16.
 */
trait Planning {
  type Plan
  type PlanId
  type Goal <: Plan
  type Assignment <: Plan
  type Activity <: Plan

  type DeletePlanRequest
  type PlanGoalRequest
  type PlanAssignmentRequest
  type PlanActivityRequest

  def createGoal:ServerOp[PlanGoalRequest, Goal]
  def createAssignment:ServerOp[PlanAssignmentRequest, Assignment]
  def createActivity:ServerOp[PlanActivityRequest, Activity]
  def deletePlan: ServerOp[DeletePlanRequest, Boolean]
}
