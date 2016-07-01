package com.mayo.planette.domain
package planning.service

import com.mayo.planette.Applicable
import com.mayo.planette.domain.ServerOperations.AuthenticationToken
import com.mayo.planette.domain.planning.model.SerializationBridge
import com.mayo.planette.domain.planning.model.abstract_dsl.PlanDSL
import com.mayo.planette.domain.ServerOperations

/**
 * @author yoav @since 6/22/16.
 *         The PlanningService has a few questionnaire agnostic aspects:
 *         1. There's a user in one end and a server on the other.
 *         2. There are questions asked by the server and answers provided by the user
 *         3. The user may want to rectify previous answers
 *         4. The server can schedule alerts and notifications for any step resulting from a user's answer.
 *
 *         Other than that every questionnaire type differ from the next one in:
 *         1. DSL
 *         2. Number of questions
 *         3. Structure(has/hasn't sub-questionnaires)
 *
 *         This implies that the service should run the questioning process, provided the questionnaire implementation which is determined by the WishListItem fed to
 *         the function tha "plan" method returns.
 *
 *
 */
trait PlanningService{

  type WishlistItem
  type PlanCategory
  //type AuthenticationToken

  val serializationBridge: SerializationBridge[Serialized[_], planDSL.PlanQuestionnaire]
  val planDSL: PlanDSL

  val applicable: Applicable[F]


  //TODO: implement polymorphic return type(ConcretePlanDescription) using type class with the trick miles sabin shows in: https://www.youtube.com/watch?v=GDbNxL8bqkY
  def plan = { token: AuthenticationToken => {
    item: WishlistItem => {
      for {
        dslImpl <- getDSLImpl(item)
        questionnaire <- getPlanQuestionnaireFromDSL(dslImpl)
        appPlan <- applicable(doQuestioning(questionnaire))
      } yield appPlan
    }
  }
  }

  def getPlanQuestionnaire = { token: AuthenticationToken => {
    item: WishlistItem => {
      for {
        dslImpl <- getDSLImpl(item)
        questionnaire <- getPlanQuestionnaireFromDSL(dslImpl)
      } yield questionnaire
    }
  }
  }

  def doQuestioning[DSLImpl <: PlanDSL]: Operation[DSLImpl#PlanQuestionnaire, DSLImpl#Plan]

  def getPlanQuestionnaireFromDSL[DSLImpl <: PlanDSL]: Operation[DSLImpl, DSLImpl#PlanQuestionnaire]

  def getPlanCategoryFromItem: Operation[WishlistItem, PlanCategory]

  def generateDSLForCategory: Operation[PlanCategory, PlanDSL]

  def getDSLImpl: Operation[WishlistItem, PlanDSL] = { item => {
    for {
      category <- getPlanCategoryFromItem(item)
      dsl <- generateDSLForCategory(category)
    } yield dsl
  }
  }
}
