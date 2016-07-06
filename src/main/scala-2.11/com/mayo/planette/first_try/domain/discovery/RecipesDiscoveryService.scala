package com.mayo.planette.first_try.domain
package discovery

import com.mayo.planette.first_try.domain.planning.model.SerializationBridge

/**
 * @author yoav @since 6/22/16.
 */
trait RecipesDiscoveryService {
  type AuthenticationToken
  type SearchQuery
  type Recipe
  type RecipeCategory

  val operations: Operations
  import operations._


  val serializationBridge: SerializationBridge[Serialized[_],RecipeCategory]

  def searchByQuery:AuthenticatedOperation[SearchQuery,List[Recipe]]

  def searchByCategory:AuthenticatedOperation[RecipeCategory, List[Recipe]]
}
