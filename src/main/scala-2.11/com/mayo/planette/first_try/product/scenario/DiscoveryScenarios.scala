package com.mayo.planette.first_try.product.scenario

import com.mayo.planette.first_try.domain.{Operations, Serialized}
import com.mayo.planette.first_try.domain.wishlists.model.{Hiking, Cooking}

import scala.util.Success

/**
 * @author yoav @since 7/1/16.
 */
trait DiscoveryScenarios extends RecipeScenario {

  val recipesDiscoveryService: ScriptServices.ScriptRecipesDiscoveryService[recipesService.Recipe]

  def createRecipeAndFindByQuery = {
    val tryCreateRecipe = createPlanAndSaveRecipe
    tryCreateRecipe match {
      case Success((token, recipe)) =>
        val searchResult = for {
          result <- recipesDiscoveryService.searchByQuery(token)(r => r.plan.isPeriodic == true)
        } yield result
        searchResult.head == recipe
    }
  }

  def createRecipesAndFindAllByCategory = {

    updateRecipe(Cooking) match{
      case Success((token1, recipe2)) =>
        updateRecipe(Hiking) match {
          case Success((token2, recipes2)) =>
            val deserializedCooking = recipesDiscoveryService.serializationBridge.toB[recipesDiscoveryService.RecipeCategory](Serialized(Cooking))
            val search = recipesDiscoveryService.searchByCategory(token1)(deserializedCooking)
            search match{
              case recipes => recipes.forall(_.plan.planCategory == deserializedCooking)

              case _ => false
            }

          case _ => false
        }
    }

  }



}
