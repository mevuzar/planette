package com.mayo.planette.domain.wishlists.model

/**
 * @author yoav @since 6/23/16.
 */
trait WishlistDSL {

  trait Questionnaire {
    def setPlanType(planType: PlanType): QuestionnaireWithPlanType
  }

  trait QuestionnaireWithPlanType extends Questionnaire {
    val planType: PlanType

    def setCategory(planCategory: PlanCategory): QuestionnaireWithPlanCategory
  }

  trait QuestionnaireWithPlanCategory extends QuestionnaireWithPlanType {
    val planCategory: PlanCategory

    def setPeriodic(periodic: Boolean): AnsweredQuestionnaire
  }

  trait AnsweredQuestionnaire extends QuestionnaireWithPlanCategory {
    val periodic: Boolean
  }

}

trait PlanType
case object PlanTypeGoal extends PlanType
case object PlanTypeActivity extends PlanType
case object PlanTypeAssignment extends PlanType

trait PlanCategory
case object Cooking extends PlanCategory
case object Hiking extends PlanCategory

