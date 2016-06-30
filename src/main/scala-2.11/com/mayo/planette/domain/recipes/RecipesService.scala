package com.mayo.planette.domain
package recipes

import com.mayo.planette.domain.planning.model.SerializationBridge
import com.mayo.planette.domain.planning.model.abstract_dsl.PlanDSL
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
  type UpdateRecipeRequest <: WithId[RecipeId]
  trait RecipeMandatoryProperties{
    val creator: RecipeCreator
  }
  val serializationBridgitte: SerializationBridge[PlanDSL#Plan, CreateRecipeRequest]

//  type Operation[A,B]
//  type AuthenticatedOperation[A,B] = AuthenticationToken => Operation[A,B]

  def createRecipe: AuthenticatedOperation[CreateRecipeRequest, Recipe]
  def updateRecipe: AuthenticatedOperation[UpdateRecipeRequest, Recipe]
  def deleteRecipe: AuthenticatedOperation[RecipeId, OperationAcknowledgement]
}
