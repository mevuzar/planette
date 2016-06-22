package com.mayo.planette.domain.discovery

/**
 * @author yoav @since 6/22/16.
 */
trait RecipesDiscoveryService {
  type AuthenticationToken
  type SearchQuery
  type Recipe
  type RecipeCategory

  type Operation[A,B]
  type AuthenticatedOperation[A,B] = AuthenticationToken => Operation[A,B]

  def searchByQuery:AuthenticatedOperation[SearchQuery,List[Recipe]]

  def searchByCategory:AuthenticatedOperation[RecipeCategory, List[Recipe]]
}
