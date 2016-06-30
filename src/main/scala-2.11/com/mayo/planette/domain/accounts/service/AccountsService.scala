package com.mayo.planette.domain
package accounts.service

import com.mayo.planette.domain.ServerOperations
import com.mayo.planette.domain.ServerOperations.AuthenticationToken


/**
 * @author yoav @since 6/21/16.
 */
trait AccountsService {
  type SignUpRequest
  type Credentials
  //type AuthenticationToken

  //type Operation[A,B]

  def signUp: Operation[SignUpRequest, AuthenticationToken]

  def signIn: Operation[Credentials,AuthenticationToken]

  def signOut: Operation[AuthenticationToken, Boolean]
}
