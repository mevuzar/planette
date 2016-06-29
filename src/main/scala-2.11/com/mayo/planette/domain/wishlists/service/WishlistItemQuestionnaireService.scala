package com.mayo.planette.domain.wishlists.service

import com.mayo.planette.domain.wishlists.model.{PlanCategory, PlanType, WishlistDSL}
import com.mayo.planette.{Applicable, Monadic}

/**
 * @author yoav @since 6/22/16.
 */
trait WishlistItemQuestionnaireService[AuthenticationToken] {

  val wishListDSLImpl: WishlistDSL

  val applicable: Applicable[F]
  type F[A] <: Monadic[A, F]
  type Operation[A, B] = A => Monadic[B, F]
  type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]

  def fillQuestionnaire: AuthenticatedOperation[wishListDSLImpl.Questionnaire, wishListDSLImpl.AnsweredQuestionnaire] = { token => { questionnaire =>
    val answer = for {
      withPlanType <- askForPlanType()
      withPeriodic <- askIfPeriodic()
      withCategory <- askForPlanCategory()
      answered <- applicable(questionnaire.setPlanType(withPlanType).setCategory(withCategory).setPeriodic(withPeriodic))
    } yield (answered)

    answer
  }
  }

  protected def askForPlanType: Operation[Unit, PlanType]

  protected def askForPlanCategory: Operation[Unit, PlanCategory]

  protected def askIfPeriodic: Operation[Unit, Boolean]

}
