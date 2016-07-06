package com.mayo.planette.first_try.product.scenario

import com.mayo.planette.first_try.domain.Serialized
import com.mayo.planette.first_try.domain.planning.model.abstract_dsl.PlanMandatoryProperties
import com.mayo.planette.first_try.domain.wishlists.model.PlanCategory
import com.mayo.planette.first_try.product.scenario.ScriptServices.ScriptingOperations

import scala.util.{Failure, Success, Try}

/**
 * Created by Owner on 6/30/2016.
 */
trait RecipeScenario extends SignUpWishAndPlanCookingScenario {

  val recipesService: ScriptServices.ScriptRecipesService

  def createPlanAndSaveRecipe = {
    val tryPlan = signUpWishAndPlanCookingScenario
    tryPlan match {
      case Success((token, plan)) =>
        val serializedPlan = Serialized(plan.get)
        Success((token, recipesService.createRecipe(token)(recipesService.serializationBridge.toB(serializedPlan))))

      case _ => Failure(new Exception("failure!!!!!"))
    }
  }

  def updateRecipe(planCategory: PlanCategory) = {
    val tryCreatedRecipe = createPlanAndSaveRecipe
    tryCreatedRecipe match {
      case Success((token, recipe)) =>

        val updatedPlan = recipesService.generateUpdateRequestFromRecipe(recipe.withPlan(recipe.id, new PlanMandatoryProperties {
          override val isPeriodic: Boolean = true
          override type Category = PlanCategory
          override val planCategory: Category = planCategory
        }))

        val result = recipesService.updateRecipe(token)(updatedPlan)
        Try((token, result))

      case _ => Failure(new Exception("failure"))
    }
  }

  def deleteScenario = {
    val recipe = mock[recipesService.Recipe]
    val token = mock[ScriptingOperations.AuthenticationToken]
    recipesService.deleteRecipe(token)(recipe.id)
  }

}
