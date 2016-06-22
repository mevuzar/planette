package com.mayo.planette.domain.discovery.model

import java.time.ZonedDateTime
import java.util.UUID
/**
 * @author yoav @since 6/22/16.
 */
object RecipesDiscoveryModel {
  sealed trait RecipeCategory
  case object Cooking
  case object Leisure
  case object Travelling
  case object RealEstate

  case class Creator(accountId: UUID, name: String)

  case class Recipe(id: Int,
                    name: String,
                    category: RecipeCategory,
                    creator: Creator,
                    createdAt: ZonedDateTime,
                    updatedAt: Option[ZonedDateTime])//TODO: smart constructor with applicative validations

}
