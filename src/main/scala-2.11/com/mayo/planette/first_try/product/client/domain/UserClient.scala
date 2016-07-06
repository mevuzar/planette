package com.mayo.planette.first_try.product.client
package domain

/**
 * @author yoav @since 6/16/16.
 */
trait UserClient {
  val accountService: ClientAccountsService
  val planningService: ClientPlanningService
  val discoveryService: ClientRecipesDiscoveryService
  val lessonsService: Lessons
  val wishlistsService: ClientWishlistsService
  val recipesService: ClientRecipesService

  def appStart: Unit

  def show[A](a: A): Unit
}

