package com.mayo.planette.domain.accounts.service


/**
 * @author yoav @since 6/21/16.
 */
trait AccountsService {
  type SignUpRequest
  type Credentials
  type AuthenticationToken

  type Operation[A,B]

  def signUp: Operation[SignUpRequest, AuthenticationToken]

  def signIn: Operation[Credentials,AuthenticationToken]

  def signOut: Operation[AuthenticationToken, Boolean]
}
