package com.mayo.planette.first_try.domain.discovery
package interpreter

import model.RecipesDiscoveryModel
/**
 * @author yoav @since 6/22/16.
 */
trait RecipesDiscoveryInterpreter extends RecipesDiscoveryService{
  override type Recipe = RecipesDiscoveryModel.Recipe
  override type RecipeCategory = RecipesDiscoveryModel.RecipeCategory
}
