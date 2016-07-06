package com.mayo.planette.first_try.product.demo.server.accounts.model

/**
 * @author yoav @since 7/4/16.
 */
object AccountsModel {
  case class AccountCredentials(userName: String, password: String)

  case class Account(credentials: AccountCredentials, name:String, mail: String)
}
