package com.mayo.planette.domain.planning.service

import com.mayo.planette.{Applicable, Monadic}
import com.mayo.planette.domain.planning.model.abstract_dsl.{PlanQuestionnaireDSL, PlanDSL}
import com.mayo.planette.domain.planning.model.SerializationBridge
import com.mayo.planette.domain.wishlists.model.{Cooking, PlanCategory, WishlistDSL}

/**
 * @author yoav @since 6/22/16.
 */
trait PlanningService {

  type AnsweredQuestionnaire
  type AuthenticationToken
  type PlanningSession <: PlanningSessionMandatoryProperties
  trait PlanningSessionMandatoryProperties{
    val questionnaireDSL: PlanQuestionnaireDSL
  }

  val serializationBridge: SerializationBridge[AnsweredQuestionnaire, planDSL.PlanQuestionnaire]
  val planDSL: PlanDSL


  val applicable: Applicable[F]
  type F[A] <: Monadic[A, F]
  type Operation[A, B] = A => Monadic[B, F]
  type AuthenticatedOperation[A, B] = AuthenticationToken => Operation[A, B]

  //TODO: implement polymorphic return type using type class with the trick miles sabin shows in: https://www.youtube.com/watch?v=GDbNxL8bqkY
  def plan: AuthenticatedOperation[AnsweredQuestionnaire, PlanningSession]

}
