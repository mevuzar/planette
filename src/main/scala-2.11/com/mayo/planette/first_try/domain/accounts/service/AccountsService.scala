package com.mayo.planette.first_try.domain
package accounts.service

import scala.util.Try


/**
 * @author yoav @since 6/21/16.
 */
trait AccountsService {
  type SignUpRequest

  type Credentials
  val operations: Operations

  import operations._

  //type AuthenticationToken

  //type Operation[A,B]

  def signUp: Operation[SignUpRequest, AuthenticationToken]

  def signIn: Operation[Credentials, Try[AuthenticationToken]]

  def signOut: Operation[AuthenticationToken, Boolean]
}
