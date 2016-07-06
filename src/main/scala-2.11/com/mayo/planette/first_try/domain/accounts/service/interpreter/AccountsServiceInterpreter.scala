package com.mayo.planette.first_try.domain
package accounts.service.interpreter


import com.mayo.planette.first_try.domain.Operations
import com.mayo.planette.first_try.domain.accounts.service.AccountsService

import scala.util.Try

/**
 * @author yoav @since 6/21/16.
 */
trait AccountsServiceInterpreter extends AccountsService {
  val operations: Operations
  import operations._


  override def signUp: Operation[SignUpRequest, AuthenticationToken] = ???

  override def signIn: Operation[Credentials, Try[AuthenticationToken]] = ???

  override def signOut: Operation[AuthenticationToken, Boolean] = ???


}
