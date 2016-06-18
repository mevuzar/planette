package com.mayo.planette.product
package scenario

import com.mayo.planette.product.domain.client.ServerOperation
import com.mayo.planette.product.domain.client.domain.{Account, UserClient}


/**
 * @author yoav @since 6/15/16.
 */
object Signup extends App {
  val client: UserClient = ???


}

case class SignUp(name: String, age: Int)

case class UserCredentials(accountId: Int, token: java.util.UUID)

object UserAccount extends Account {
  override type SignUpRequest = SignUp
  override type SignInRequest = UserCredentials
  override type SignOutRequest = UserCredentials
  override type Credentials = UserCredentials

  override def signUp: ServerOperation[SignUpRequest, Credentials] = ???

  override def signIn: ServerOperation[SignInRequest, Credentials] = ???

  override def signOut: ServerOperation[SignOutRequest, Boolean] = ???
}