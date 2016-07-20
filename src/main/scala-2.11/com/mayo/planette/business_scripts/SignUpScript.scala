package com.mayo.planette.business_scripts

import com.mayo.planette.abstraction.production.client.accounts.AccountsUserInteractions
import com.mayo.planette.abstraction.terminology.client.Question
import com.mayo.planette.business_scripts.interpreters.{StdInInteractionInterpreter, UserAccounts}

import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/6/16.
 */
object SignUpScript extends App {

  //lazy val system = ActorSystem("SignUpScript")

  object AccountsUserInteraction extends AccountsUserInteractions

  lazy val userInputToSignInRequest = UserAccounts.signIn.compose((interpreter: ~>[Question, Id.Id]) => (UserAccounts.dataStore.getAccount.get.token, AccountsUserInteraction.askAccountCredentials(interpreter)))
  lazy val userInputToSignUpRequest = UserAccounts.signUp.compose(AccountsUserInteraction.askSignUpDetails)

}
