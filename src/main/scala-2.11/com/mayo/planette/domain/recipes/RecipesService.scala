package com.mayo.planette.domain.recipes

import com.mayo.planette.domain.WithId

/**
 * @author yoav @since 6/22/16.
 */
trait RecipesService {
  type AuthenticationToken
  type Recipe <: RecipeMandatoryProperties with WithId[RecipeId]
  type RecipeId
  type RecipeCreator
  type OperationAcknowledgement
  type CreateRecipeRequest <: RecipeMandatoryProperties
  type UpdateRecipeRequest <: WithId[RecipeId]
  trait RecipeMandatoryProperties{
    val creator: RecipeCreator
  }


  type Operation[A,B]
  type AuthenticatedOperation[A,B] = AuthenticationToken => Operation[A,B]

  def createRecipe: AuthenticatedOperation[CreateRecipeRequest, Recipe]
  def updateRecipe: AuthenticatedOperation[UpdateRecipeRequest, Recipe]
  def deleteRecipe: AuthenticatedOperation[RecipeId, OperationAcknowledgement]
}
