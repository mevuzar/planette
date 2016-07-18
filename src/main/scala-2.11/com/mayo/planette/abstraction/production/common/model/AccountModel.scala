package com.mayo.planette.abstraction.production.common.model

import java.util.UUID

import scala.util.{Failure, Success, Try}

/**
 * @author yoav @since 7/6/16.
 */
object AccountModel {

  case class UserSignupDetails(name: String, userName: String, password: String, mail: String)

  case class AccountCredentials(userName: String, password: String)

  case class UserAccount(id:UUID, credentials: AccountCredentials, token: UUID, name:String, mail: String)

  object UserAccount{
    def generateAccount(credentials: AccountCredentials, name:String, mail: String): Try[UserAccount] = {
      def validateMail: Boolean = mail.nonEmpty //TODO: implement mail regex validation

      if(validateMail)Success(UserAccount(UUID.randomUUID, credentials, UUID.randomUUID, name, mail)) //TODO: implement validation using Applicative of Validations
      else Failure(InvalidMailException(mail))
    }

    def revalidateToken(account: UserAccount): UserAccount = account.copy(token = UUID.randomUUID)
  }
  case class InvalidMailException(mail: String) extends Exception(s"mail $mail has an invalid format")

  case class UserToken(accountId: UUID, token: UUID) //TODO: implement token as JWT

}
