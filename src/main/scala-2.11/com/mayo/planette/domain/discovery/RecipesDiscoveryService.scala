package com.mayo.planette.domain
package discovery

import com.mayo.planette.domain.planning.model.SerializationBridge

/**
 * @author yoav @since 6/22/16.
 */
trait RecipesDiscoveryService {
  type AuthenticationToken
  type SearchQuery
  type Recipe
  type RecipeCategory

  val serializationBridge: SerializationBridge[Serialized[_],RecipeCategory]

  def searchByQuery:AuthenticatedOperation[SearchQuery,List[Recipe]]

  def searchByCategory:AuthenticatedOperation[RecipeCategory, List[Recipe]]
}
