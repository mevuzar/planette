package com.mayo.planette.product.client
package domain

/**
 * @author yoav @since 6/16/16.
 */
trait UserClient {
  val accountService: ClientAccountsService
  val planningService: ClientPlanningService
  val discoveryService: Discovery
  val lessonsService: Lessons
  val wishlistsService: ClientWishlistsService
  val recipesService: ClientRecipesService

  def appStart: Unit

  def show[A](a: A): Unit
}

