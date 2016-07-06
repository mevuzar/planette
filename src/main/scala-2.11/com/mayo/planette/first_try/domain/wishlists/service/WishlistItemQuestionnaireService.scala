package com.mayo.planette.first_try.domain
package wishlists.service

import com.mayo.planette.Applicable
import com.mayo.planette.first_try.domain.wishlists.model.{PlanCategory, PlanType, WishlistDSL}

/**
 * @author yoav @since 6/22/16.
 */
trait WishlistItemQuestionnaireService {

  val operations: Operations
  import operations._

  val wishListDSLImpl: WishlistDSL

  def fillQuestionnaire: AuthenticatedOperation[wishListDSLImpl.Questionnaire, FA[wishListDSLImpl.AnsweredQuestionnaire]] = { token => { questionnaire =>
    val answer = for {
      withPlanType <- FA(askForPlanType())
      withPeriodic <- FA(askIfPeriodic())
      withCategory <- FA(askForPlanCategory())
    //answered <- questionnaire.setPlanType(withPlanType).setCategory(withCategory).setPeriodic(withPeriodic)
    } yield questionnaire.setPlanType(withPlanType).setCategory(withCategory).setPeriodic(withPeriodic)

    answer
  }
  }

  protected def askForPlanType: Operation[Unit, PlanType]

  protected def askForPlanCategory: Operation[Unit, PlanCategory]

  protected def askIfPeriodic: Operation[Unit, Boolean]

}
