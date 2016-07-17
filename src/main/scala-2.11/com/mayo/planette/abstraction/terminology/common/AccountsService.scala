package com.mayo.planette.abstraction.terminology.common

import com.mayo.planette.abstraction.terminology.{AuthenticatedOperations, CommonTerms, CommonOperations}

import scala.util.Try


/**
 * @author yoav @since 6/21/16.
 */
private[abstraction] trait AccountsService extends CommonOperations with CommonTerms{

  type Account
  type SignUpRequest
  type SignInRequest
  type SignOutRequest
  type Credentials
  type AccountId
  

//  def signUpRequestToAccount: SignUpRequest => Account
//  def signUpRequestToAuthenticationToken: SignUpRequest => AuthenticationToken
//  def credentialsToToken: Credentials => AuthenticationToken
//  def signInRequestToAccountId: SignInRequest => AccountId
//  def signInRequestToCredentials: SignInRequest => Credentials
//  def accountToCredentials: Account => Credentials
//  def accountToToken: Account => AuthenticationToken
//  def setAccountCredentials: Credentials => Account => Account
//  def signOutRequestToAccountId: SignOutRequest => AccountId

  def signUp: Operation[SignUpRequest, AuthenticationToken]

  def signIn: Operation[SignInRequest, Try[AuthenticationToken]]

  def signOut: Operation[SignOutRequest, Boolean]
}
