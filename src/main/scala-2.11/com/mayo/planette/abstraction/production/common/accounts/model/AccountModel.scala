package com.mayo.planette.abstraction.production.common.accounts.model

import scala.util.{Failure, Success, Try}
import java.util.UUID

/**
 * @author yoav @since 7/6/16.
 */
object AccountModel {

  case class UserSignupDetails(userName: String, password: String, mail: String)

  case class AccountCredentials(userName: String, password: String)

  case class UserAccount(uuid:UUID, credentials: AccountCredentials, name:String, mail: String)

  object UserAccount{
    def generateAccount(uuid: UUID, credentials: AccountCredentials, name:String, mail: String): Try[UserAccount] = {
      def validateMail: Boolean = mail.nonEmpty //TODO: implement mail regex validation

      if(validateMail)Success(UserAccount(uuid, credentials, name, mail)) //TODO: implement validation using Applicative of Validations
      else Failure(InvalidMailException(mail))
    }
  }
  case class InvalidMailException(mail: String) extends Exception(s"mail $mail has an invalid format")

  case class UserToken(accountId: UUID, token: UUID) //TODO: implement token as JWT

}
