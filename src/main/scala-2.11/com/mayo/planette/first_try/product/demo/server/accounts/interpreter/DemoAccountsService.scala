package com.mayo.planette.first_try.product.demo.server
package accounts.interpreter

import java.util.UUID

import com.mayo.planette.first_try.domain.Operations
import com.mayo.planette.first_try.domain.accounts.service.AccountsService
import com.mayo.planette.first_try.product.demo.server.accounts.model.AccountsModel.{Account, AccountCredentials}


import scala.collection.mutable.HashMap
import scala.util.{Success, Try}

/**
 * @author yoav @since 7/4/16.
 */
trait DemoAccountsService extends AccountsService {

  val operations = ServerOperations
  import operations._

  override type Credentials = AccountCredentials
  override type SignUpRequest = Account


  val map = HashMap.empty[String, Account]

  override def signUp: Operation[SignUpRequest, AuthenticationToken] = { request =>
    val token = UUID.randomUUID.toString
    map.put(token, request)
    token
  }

  override def signIn: Operation[Credentials, Try[AuthenticationToken]] = { credentials => {
    Try(map.find {
      case (token, account) => {
        account.credentials.userName == credentials.userName &&
          account.credentials.password == credentials.password
      }
    }.get._1)
  }
  }

  override def signOut: Operation[AuthenticationToken, Boolean] = ???
}
