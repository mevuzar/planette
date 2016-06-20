package com.mayo.planette.product
package scenario


import java.util.concurrent.TimeUnit

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props, ActorSystem, ActorSelection}
import akka.util.Timeout
import com.mayo.planette.product.domain.client.{CommunicationProtocol, ServerOperation}
import com.mayo.planette.product.domain.client.domain.{UserInteraction, Account, UserClient}

import scala.concurrent.Future


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

  override def signUp: ServerOperation[SignUpRequest, Credentials] = ???

  override def signIn: ServerOperation[SignInRequest, Credentials] = ???

  override def signOut: ServerOperation[SignOutRequest, Boolean] = ???
}

trait PlainInMemoryCommunicationProtocol[Request] extends CommunicationProtocol[Request, Future[String]] with Actor{
  override type Address = ActorSelection
  val system = ActorSystem("plain-in-memory-communication-protocol")
  implicit val ctxt = system.dispatcher

  override def run(address: Address): (Request) => Future[String] = {
    def ask(request: Request) = {
      import akka.pattern.ask
      //val futureResult = system.actorOf(Props[ActorA]) ? request//address.resolveOne()(Timeout(5, TimeUnit.SECONDS)).flatMap(_.ask(request))
      //futureResult.map(_.toString)

      Future("")
    }

    ask
  }
}

//case class InMemoryServerOperation[Request](request: Request, address: ActorSelection) extends
//PlainInMemoryCommunicationProtocol[Request]{
//
//}

class ActorA extends Actor{
  override def receive: Receive = ???
}