package com.mayo.planette.product.scenario

import com.mayo.planette.Applicable
import com.mayo.planette.domain.ServerOperations.AuthenticationToken
import com.mayo.planette.domain.discovery.model.RecipesDiscoveryModel.Cooking
import com.mayo.planette.domain.planning.model.abstract_dsl.PlanMandatoryProperties
import com.mayo.planette.domain.wishlists.model.PlanCategory
import com.mayo.planette.domain.{F, Serialized}

import scala.util.{Failure, Success, Try}

/**
 * Created by Owner on 6/30/2016.
 */
trait RecipeScenario extends SignUpWishAndPlanCookingScenario {

  val recipesService: ScriptServices.ScriptRecipesService
  val applicable: Applicable[F]

  def createPlanAndSaveRecipe = {
    val tryPlan = signUpWishAndPlanCookingScenario
    tryPlan match {
      case Success((token, plan)) =>
        val serializedPlan = Serialized(plan.get)
        Success((token, recipesService.createRecipe(token)(recipesService.serializationBridge.toB(serializedPlan)).get))

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
        Try((token, result.get))

      case _ => Failure(new Exception("failure"))
    }
  }

  def deleteScenario = {
    val recipe = mock[recipesService.Recipe]
    val token = mock[AuthenticationToken]
    recipesService.deleteRecipe(token)(recipe.id)
  }

}
