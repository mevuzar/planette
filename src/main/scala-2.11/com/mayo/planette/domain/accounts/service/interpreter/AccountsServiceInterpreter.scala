package com.mayo.planette.domain.accounts.service.interpreter

import com.mayo.planette.domain.Operation
import com.mayo.planette.domain.ServerOperations.AuthenticationToken
import com.mayo.planette.domain.accounts.service.AccountsService

/**
 * @author yoav @since 6/21/16.
 */
trait AccountsServiceInterpreter extends AccountsService {
  override def signUp: Operation[SignUpRequest, AuthenticationToken] = ???

  override def signIn: Operation[Credentials, AuthenticationToken] = ???

  override def signOut: Operation[AuthenticationToken, Boolean] = ???


}
