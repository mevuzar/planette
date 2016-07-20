package com.mayo.planette.abstraction.production.client.accounts

import com.mayo.planette.abstraction.production.common.model.AccountModel
import com.mayo.planette.abstraction.production.common.model.AccountModel.{AccountCredentials, UserSignupDetails}
import com.mayo.planette.abstraction.terminology.client.PreDefInteractions._
import com.mayo.planette.abstraction.terminology.client.UserInteractions._
import com.mayo.planette.abstraction.terminology.client.{PreDefInteractions, Question, UserInteractions}

import scalaz.{Free, Id, ~>}

/**
 * @author yoav @since 7/16/16.
 */
trait AccountsUserInteractions {

  def askName = interact(StringInteraction("what's your name?"))

  def askUserName = interact(StringInteraction("please enter a user name"))

  def askPassword = interact(StringInteraction("please enter a password"))

  def askMail = interact(StringInteraction("please enter a mail address"))

  def askSignUpDetails(interpreter: Question ~> Id.Id) = {
    val script = for{
      name <- askName
      userName <- askUserName
      password <- askPassword
      mail <- askMail
    } yield (name, userName, password, mail)

    val interpreted = Free.runFC(script)(interpreter)
    UserSignupDetails.tupled(interpreted)
  }

  def askAccountCredentials(interpreter: Question ~> Id.Id) = {
    val script = for{
      userName <- askUserName
      password <- askPassword
    } yield (userName, password)

    val interpreted = Free.runFC(script)(interpreter)
    AccountCredentials.tupled(interpreted)
  }

}
