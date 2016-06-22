package com.mayo.planette.domain.recipes

import com.mayo.planette.domain.WithId

/**
 * @author yoav @since 6/22/16.
 */
trait RecipesService {

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

  def createRecipe: Operation[CreateRecipeRequest, Recipe]
  def updateRecipe: Operation[UpdateRecipeRequest, Recipe]
  def deleteRecipe: Operation[RecipeId, OperationAcknowledgement]
}
