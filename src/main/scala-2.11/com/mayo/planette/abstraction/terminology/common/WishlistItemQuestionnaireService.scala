package com.mayo.planette.abstraction.terminology.common

import com.mayo.planette.abstraction.terminology.{AuthenticatedOperations, CommonOperations, CommonTerms}

/**
 * @author yoav @since 6/22/16.
 */
private[abstraction] trait WishlistItemQuestionnaireService extends CommonOperations with AuthenticatedOperations with CommonTerms {

  type EmptyQuestionnaire
  type AnsweredQuestionnaire
  type PlanType
  type PlanCategory

  def fillQuestionnaire: AuthenticatedOperation[EmptyQuestionnaire, AnsweredQuestionnaire]

  protected def askForPlanType: Operation[Unit, PlanType]

  protected def askForPlanCategory: Operation[Unit, PlanCategory]

  protected def askIfPeriodic: Operation[Unit, Boolean]

}
