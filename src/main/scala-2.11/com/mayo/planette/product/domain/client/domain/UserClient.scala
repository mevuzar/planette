package com.mayo.planette.product.domain.client
package domain

/**
 * @author yoav @since 6/16/16.
 */
trait UserClient extends 
Account with
Planning with
Discovery with 
Lessons with 
Wishlists with
Recipes{
  def appStart: Unit

  def show[A](a: A): Unit
}

