package com.mayo.planette.domain.planning

import com.mayo.whatis.mean.Meaning

import scala.concurrent.Future

/**
 * @author yoav @since 5/6/16.
 */
trait PlannerService {
  type PlanningSession <: PlanningSessionMandatoryProperties
  type PlanRequest
  type Plan

  def requestPlan(request: PlanRequest): Future[Option[Plan]]

  def startPlanning(plan: Plan): Future[PlanningSession]
}

trait PlanMandatoryProperties {
  type Phase
  type PlanRequiredInfo = (Meaning, Option[Meaning])
  val name: String
  val neededInfo: Seq[PlanRequiredInfo]
  val phases : Seq[Phase]
}

trait PlanningSessionMandatoryProperties{
  val plan: PlanMandatoryProperties

}