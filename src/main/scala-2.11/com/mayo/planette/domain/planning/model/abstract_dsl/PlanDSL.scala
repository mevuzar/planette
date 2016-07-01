package com.mayo.planette.domain.planning.model.abstract_dsl

import com.mayo.planette.domain.wishlists.model.{PlanType, PlanCategory}

/**
 * @author yoav @since 6/22/16.
 */
trait PlanDSL {
  type Plan <: PlanMandatoryProperties
  type Goal <: Plan with GoalMandatoryProperties
  type Activity <: Plan
  type Assignment <: Plan
  trait GoalMandatoryProperties{
    type GoalTerm
    trait GoalType
    case object PeriodicActivity extends GoalType
    case object PeriodicAssignment extends GoalType
    case object Achievement extends GoalType
    
    val term: GoalTerm
    val goalType: GoalType
  }


  type PlanQuestionnaire
  type PQGoalAspect
  type PQAssignmentAspect
  type ActivityQuestionnaire

}

trait PlanMandatoryProperties extends Periodic

trait Periodic{
  val isPeriodic: Boolean
}
