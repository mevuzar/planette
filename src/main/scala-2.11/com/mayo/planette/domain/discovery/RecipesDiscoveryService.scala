package com.mayo.planette.domain
package discovery

/**
 * @author yoav @since 6/22/16.
 */
trait RecipesDiscoveryService {
  type AuthenticationToken
  type SearchQuery
  type Recipe
  type RecipeCategory


  def searchByQuery:AuthenticatedOperation[SearchQuery,List[Recipe]]

  def searchByCategory:AuthenticatedOperation[RecipeCategory, List[Recipe]]
}
