package com.mayo.planette.domain.accounts.service.interpreter

import com.mayo.planette.domain.ServerOperation
import com.mayo.planette.domain.accounts.model.AccountModel.{UserCredentials, UserToken}
import com.mayo.planette.domain.accounts.service.AccountsService

/**
 * @author yoav @since 6/21/16.
 */
trait AccountsServiceInterpreter extends AccountsService{
  override type AuthenticationToken = UserToken
  override type Credentials = UserToken
  override type SignUpRequest = UserCredentials

  override type Operation[A,B] = ServerOperation[A,B]

  override def signUp: ServerOperation[UserCredentials, UserToken] = ???

  override def signIn: ServerOperation[UserToken, UserToken] = ???

  override def signOut: ServerOperation[UserToken, Boolean] = ???
}
