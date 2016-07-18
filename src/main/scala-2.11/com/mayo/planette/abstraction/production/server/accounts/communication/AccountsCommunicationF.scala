package com.mayo.planette.abstraction.production.server.accounts.communication

import java.util.UUID

import com.mayo.planette.abstraction.production.common.model.AccountModel
import AccountModel.{AccountCredentials, UserSignupDetails, UserToken}
import com.mayo.planette.abstraction.terminology.FutureStringOr
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceMethodCall
import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperations._

import scala.util.Try

/**
 * @author yoav @since 7/13/16.
 */
object AccountsCommunicationF {

  sealed trait AccountsMethodCall[+A] extends ServiceMethodCall[A]

  case class SignUpCall(signUpDetails: UserSignupDetails) extends AccountsMethodCall[FutureStringOr[UserToken]]

  case class SignInCall(signInDetails: (UUID, AccountCredentials)) extends AccountsMethodCall[FutureStringOr[Try[UserToken]]]

  case class SignOutCall(accountId: UUID) extends AccountsMethodCall[FutureStringOr[Boolean]]

  object AccountsCommunicationOperations {
    def signUp(signupDetails: UserSignupDetails) = serviceOperation(SignUpCall(signupDetails))

    def signIn(signInDetails: (UUID, AccountCredentials)) = serviceOperation(SignInCall(signInDetails))

    def signOut(accountId: UUID) = serviceOperation(SignOutCall(accountId))
  }

}
