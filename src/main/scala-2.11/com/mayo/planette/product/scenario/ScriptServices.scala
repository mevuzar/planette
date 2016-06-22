package com.mayo.planette.product.scenario

import com.mayo.planette.domain.WithId
import com.mayo.planette.product.client.domain.{UserWishlistsService, Account}

import scala.util.Try

/**
 * @author yoav @since 6/22/16.
 */
object ScriptServices {
  trait ScriptAccount extends Account {
    override type Operation[A, B] = A => Try[B]
  }

  trait ScriptUserWishlistsService[AccountsToken] extends UserWishlistsService {
    override type AuthenticationToken = AccountsToken
    override type Operation[A, B] = A => Try[B]
    override type WishlistCreate <: ScriptWishlistMandatoryProperties
    override type WishlistUpdate <: ScriptWishlistUpdateProperties with WithId[WishlistId]
    trait ScriptWishlistMandatoryProperties extends WishlistMandatoryProperties{
      def addItemsToList(items: List[WishlistItem]): WishlistCreate
    }
    trait ScriptWishlistUpdateProperties extends WishlistUpdateMandatoryProperties{
      def addItemsToAddList(items: List[WishlistItem]): WishlistUpdate
      def addItemsToRemoveList(items: List[WishlistItem]): WishlistUpdate
    }

  }

}
