package com.mayo.planette.product.scenario

import com.mayo.planette.product.ScriptMocker
import ScriptServices.{ScriptAccount, ScriptUserWishlistsService}

import scala.util.Success

/**
 * @author yoav @since 6/22/16.
 */
trait WishlistScenarios extends ScriptMocker{

  val account: ScriptAccount
  val wishlists: ScriptUserWishlistsService[account.AuthenticationToken]

  val token = mock[account.AuthenticationToken]

  val createWishlistWithItems = for{
    q1 <- wishlists.userFillQuestionaire()
    q2 <- wishlists.userFillQuestionaire()
    q3 <- wishlists.userFillQuestionaire()
    item1 <- wishlists.createWishlistItem(token)(q1)
    item2 <- wishlists.createWishlistItem(token)(q2)
    item3 <- wishlists.createWishlistItem(token)(q3)
    createRequest <- Success(mock[wishlists.WishlistCreate].addItemsToList(List(item1, item2, item3)))
    createWishlist <- wishlists.createWishlist(token)(createRequest)
  } yield(createWishlist)

  createWishlistWithItems match{
    case Success(wishlist) if wishlist.items.size == 3 => println("success")
    case _ => println("failure")
  }
}

