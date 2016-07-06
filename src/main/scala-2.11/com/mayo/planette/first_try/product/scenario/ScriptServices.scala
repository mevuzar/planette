package com.mayo.planette.first_try.product.scenario

import com.mayo.planette.Monadic
import com.mayo.planette.first_try.domain.{Operations, Serialized, WithId}
import com.mayo.planette.first_try.domain.wishlists.service.WishlistItemQuestionnaireService
import com.mayo.planette.first_try.product.client.domain.{ClientRecipesDiscoveryService, ClientRecipesService, ClientAccountsService, ClientPlanningService, ClientWishlistsService}

import scala.util.Try

/**
 * @author yoav @since 6/22/16.
 */
object ScriptServices {

  object ScriptingOperations extends Operations {
    override type AuthenticationToken = String
  }

  trait ScriptAccountService extends ClientAccountsService {
    override val operations = ScriptingOperations
  }

  trait ScriptWishlistsService extends ClientWishlistsService {
    override val operations = ScriptingOperations

    override type WishlistCreate <: ScriptWishlistMandatoryProperties
    override type WishlistUpdate <: ScriptWishlistUpdateProperties with WithId[WishlistId]

    trait ScriptWishlistMandatoryProperties extends WishlistMandatoryProperties {
      def addItemsToList(items: List[WishlistItem]): WishlistCreate
    }

    trait ScriptWishlistUpdateProperties extends WishlistUpdateMandatoryProperties {
      def addItemsToAddList(items: List[WishlistItem]): WishlistUpdate

      def addItemsToRemoveList(items: List[WishlistItem]): WishlistUpdate
    }

    override val listItemQuestionnaireService: ScriptWIQuestionnaireService

    trait ScriptWIQuestionnaireService extends WishlistItemQuestionnaireService {
      override val operations = ScriptingOperations
    }

  }


  trait ScriptPlanningService[WLItem] extends ClientPlanningService {
    override val operations = ScriptingOperations
    override type WishlistItem = WLItem
  }


  trait ScriptRecipesService extends ClientRecipesService{
    override val operations = ScriptingOperations
    def generateUpdateRequestFromRecipe(recipe: RecipeMandatoryProperties): UpdateRecipeRequest
  }

  trait ScriptRecipesDiscoveryService[TRecipe] extends ClientRecipesDiscoveryService{
    override val operations = ScriptingOperations
    override type Recipe = TRecipe
    override type RecipeCategory
    override type SearchQuery = TRecipe => Boolean
  }
}
