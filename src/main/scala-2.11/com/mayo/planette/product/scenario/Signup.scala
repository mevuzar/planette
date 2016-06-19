package com.mayo.planette.product
package scenario

import com.mayo.planette.product.domain.client.{CommunicationProtocol, ServerOperation}
import com.mayo.planette.product.domain.client.domain.{UserInteraction, Account, UserClient}


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

  //val communication: CommunicationProtocol = ???

  override def signUp: ServerOperation[SignUpRequest, Credentials] = { communication =>

  }

  override def signIn: ServerOperation[SignInRequest, Credentials] = ???

  override def signOut: ServerOperation[SignOutRequest, Boolean] = ???
}