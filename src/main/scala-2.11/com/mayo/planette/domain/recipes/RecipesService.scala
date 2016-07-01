package com.mayo.planette.domain
package recipes

import com.mayo.planette.domain.planning.model.SerializationBridge
import com.mayo.planette.domain.planning.model.abstract_dsl.{PlanMandatoryProperties, PlanDSL}
import com.mayo.planette.domain.{ServerOperations, WithId}

/**
 * @author yoav @since 6/22/16.
 */
trait RecipesService{

  type Recipe <: RecipeMandatoryProperties with WithId[RecipeId]
  type RecipeId
  type RecipeCreator
  type OperationAcknowledgement
  type CreateRecipeRequest <: RecipeMandatoryProperties
  type UpdateRecipeRequest <: RecipeMandatoryProperties with WithId[RecipeId]
  trait RecipeMandatoryProperties{
    val creator: RecipeCreator
    val plan: PlanMandatoryProperties

    def withPlan(id: RecipeId, planMandatoryProperties: PlanMandatoryProperties): Recipe
  }
  val serializationBridge: SerializationBridge[Serialized[_], CreateRecipeRequest]

  def createRecipe: AuthenticatedOperation[CreateRecipeRequest, Recipe]
  def updateRecipe: AuthenticatedOperation[UpdateRecipeRequest, Recipe]
  def deleteRecipe: AuthenticatedOperation[RecipeId, OperationAcknowledgement]
}
