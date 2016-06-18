package com.mayo.planette.product.domain.client
package domain


/**
 * @author yoav @since 6/17/16.
 */
trait Account {
  type SignUpRequest
  type SignInRequest
  type SignOutRequest
  type Credentials

  def signUp: ServerOperation[SignUpRequest, Credentials]

  def signIn: ServerOperation[SignInRequest, Credentials]

  def signOut: ServerOperation[SignOutRequest, Boolean]
}
