package com.mayo.planette.product.client
package domain

/**
 * @author yoav @since 6/16/16.
 */
trait UserClient extends 
ClientAccountsService with
ClientPlanningService with
Discovery with 
Lessons with 
ClientWishlistsService with
ClientRecipesService{
  def appStart: Unit

  def show[A](a: A): Unit
}

