package com.mayo.planette.business_scripts.interpreters

import com.mayo.planette.abstraction.production.server.accounts.communication.AccountsCommunicationF.{SignOutCall, SignInCall, SignUpCall, AccountsMethodCall}
import com.mayo.planette.abstraction.production.server.accounts.interpreter.AccountsServiceProduction
import com.mayo.planette.abstraction.terminology.ServiceDSL.{ServerCall, ServiceOperation}
import java.util.UUID
import scala.concurrent.Future
import scalaz.{~>, Id}

/**
 * @author yoav @since 7/13/16.
 */
class DirectServerCommunication(val accountsService: AccountsServiceProduction) extends (ServiceOperation ~> Id.Id){
  override def apply[A](fa: ServiceOperation[A]): Id.Id[A] = fa match {
    case ServerCall(t: AccountsMethodCall[A]) => t match{
      case SignUpCall(details) => Right(accountsService.signUp(details))
      case SignInCall((id, credentials)) => Right(accountsService.signIn((id, credentials)))
      case SignOutCall(accountId) => Right(accountsService.signOut(accountId))
    }
  }
}
