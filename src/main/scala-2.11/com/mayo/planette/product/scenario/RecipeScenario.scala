package com.mayo.planette.product.scenario

import com.mayo.planette.Applicable
import com.mayo.planette.domain.{Serialized, F}
import com.mayo.planette.domain.planning.model.abstract_dsl.{PlanMandatoryProperties, PlanDSL}
import com.mayo.planette.product.ScriptMocker

import scala.util.{Try, Failure, Success}

/**
 * Created by Owner on 6/30/2016.
 */
trait RecipeScenario extends SignUpWishAndPlanCookingScenario{

  val recipesService: ScriptServices.ScriptRecipesService
  val applicable: Applicable[F]

  def createPlanAndSaveRecipe = {
    val tryPlan = signUpWishAndPlanCookingScenario
    tryPlan match{
      case Success((token, plan)) =>
        val serializedPlan = Serialized(plan.get)
        Success((token,recipesService.createRecipe(token)(recipesService.serializationBridge.toB(serializedPlan)).get))

      case _ => Failure(new Exception("failure!!!!!"))
    }
  }

  def updateRecipe = {
    val tryCreatedRecipe = createPlanAndSaveRecipe
    tryCreatedRecipe match{
      case Success((token,recipe)) =>

        val updatedPlan = recipesService.generateUpdateRequestFromRecipe(recipe.withPlan(recipe.id, new PlanMandatoryProperties {
          override val isPeriodic: Boolean = true
        }))

        val result = recipesService.updateRecipe(token)(updatedPlan)
        Try(result.get)

      case _ => Failure(new Exception("failure"))
    }
  }


}
