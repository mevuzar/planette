package com.mayo.planette.domain.accounts.model

import java.util.UUID

import scala.util.{Failure, Success, Try}

/**
 * @author yoav @since 6/21/16.
 */
object AccountModel {

  case class UserCredentials(userName: String, password: String)

  case class UserSignupDetails(userName: String, password: String, mail: String)

  case class Account(uuid: UUID, userName: String, password: String, mail: String)
  object Account{
    def generateAccount(uuid: UUID, userName: String, password: String, mail: String): Try[Account] = {
      def validateMail: Boolean = mail.nonEmpty //TODO: implement mail regex validation

      if(validateMail)Success(Account(uuid, userName, password, mail)) //TODO: implement validation using Applicative of Validations
      else Failure(InvalidMailException(mail))
    }
  }
  case class InvalidMailException(mail: String) extends Exception(s"mail $mail has an invalid format")

  case class UserToken(accountId: UUID, token: UUID) //TODO: implement token as JWT


}
