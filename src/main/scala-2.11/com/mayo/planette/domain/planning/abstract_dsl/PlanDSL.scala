package com.mayo.planette.domain.planning.abstract_dsl

/**
 * @author yoav @since 6/22/16.
 */
trait PlanDSL {
  type Plan <: Periodic
  type Gole <: Plan with GoleMandatoryProperties
  type GoleTerm
  type Activity <: Plan
  type Assignment <: Plan
  type PlanCategory

  trait Periodic{
    val isPeriodic: Boolean
  }

  trait GoleMandatoryProperties{
    val term: GoleTerm
  }

}
