package com.mayo.planette.abstraction.terminology.common

import com.mayo.planette.abstraction.terminology.{AuthenticatedOperations, CommonTerms, CommonOperations}
import com.mayo.planette.first_try.domain.planning.model.SerializationBridge
import com.mayo.planette.first_try.domain.planning.model.abstract_dsl.PlanDSL

import scala.util.Try

/**
 * @author yoav @since 6/22/16.
 *
 */
private[abstraction] trait PlanningService extends CommonOperations with AuthenticatedOperations with CommonTerms{

  type WishlistItem
  type PlanCategory
  type PlanQuestionnaire
  type Plan


  //TODO: implement polymorphic return type(ConcretePlanDescription) using type class with the trick miles sabin shows in: https://www.youtube.com/watch?v=GDbNxL8bqkY
  def plan: AuthenticatedOperation[WishlistItem, Plan]

  def getPlanQuestionnaire: Operation[WishlistItem, PlanQuestionnaire]

  def doQuestioning: Operation[PlanQuestionnaire, Plan]

  def getPlanQuestionnaireFromProvider[PlanQuestionnaireProvider]: Operation[PlanQuestionnaireProvider, PlanQuestionnaire]

  def getPlanCategoryFromItem: Operation[WishlistItem, PlanCategory]

  def generateQuestionnaireProviderForCategory[PlanQuestionnaireProvider]: Operation[PlanCategory, PlanQuestionnaireProvider]

}
