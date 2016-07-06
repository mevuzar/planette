package com.mayo.planette.first_try.domain
package recipes

import com.mayo.planette.first_try.domain.planning.model.SerializationBridge
import com.mayo.planette.first_try.domain.planning.model.abstract_dsl.PlanMandatoryProperties

/**
 * @author yoav @since 6/22/16.
 */
trait RecipesService {

  val operations: Operations

  import operations._

  type Recipe <: RecipeMandatoryProperties with WithId[RecipeId]
  type RecipeId
  type RecipeCreator
  type OperationAcknowledgement
  type CreateRecipeRequest <: RecipeMandatoryProperties
  type UpdateRecipeRequest <: RecipeMandatoryProperties with WithId[RecipeId]

  trait RecipeMandatoryProperties {
    val creator: RecipeCreator
    val plan: PlanMandatoryProperties

    def withPlan(id: RecipeId, planMandatoryProperties: PlanMandatoryProperties): Recipe
  }

  val serializationBridge: SerializationBridge[Serialized[_], CreateRecipeRequest]

  def createRecipe: AuthenticatedOperation[CreateRecipeRequest, Recipe]

  def updateRecipe: AuthenticatedOperation[UpdateRecipeRequest, Recipe]

  def deleteRecipe: AuthenticatedOperation[RecipeId, OperationAcknowledgement]
}
