package com.mayo.planette.product.domain.client
package domain

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

  def createGoal:ServerOperation[PlanGoalRequest, Goal]
  def createAssignment:ServerOperation[PlanAssignmentRequest, Assignment]
  def createActivity:ServerOperation[PlanActivityRequest, Activity]
  def deletePlan: ServerOperation[DeletePlanRequest, Boolean]
}
