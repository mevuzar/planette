package com.mayo.planette.product.scenario

import com.mayo.planette.Monadic
import com.mayo.planette.domain.{Serialized, WithId}
import com.mayo.planette.domain.wishlists.service.WishlistItemQuestionnaireService
import com.mayo.planette.product.client.domain.{ClientRecipesDiscoveryService, ClientRecipesService, ClientAccountsService, ClientPlanningService, ClientWishlistsService}

import scala.util.Try

/**
 * @author yoav @since 6/22/16.
 */
object ScriptServices {

  trait ScriptAccountService extends ClientAccountsService {
    //override type Operation[A, B] = A => Try[B]
  }

  trait ScriptWishlistsService extends ClientWishlistsService {
    //override type AuthenticationToken = AccountsToken
    //override type Operation[A, B] = A => Try[B]
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

     // trait ScriptMonadic[A] extends Monadic[A, F]

     // override type F[A] = ScriptMonadic[A]

    }

  }


  trait ScriptPlanningService[WLItem] extends ClientPlanningService {
    override type WishlistItem = WLItem
    //override type AuthenticationToken = AccountToken

  }


  trait ScriptRecipesService extends ClientRecipesService{
    def generateUpdateRequestFromRecipe(recipe: RecipeMandatoryProperties): UpdateRecipeRequest
  }

  trait ScriptRecipesDiscoveryService[TRecipe] extends ClientRecipesDiscoveryService{
    override type Recipe = TRecipe
    override type RecipeCategory
    override type SearchQuery = TRecipe => Boolean
  }
}
