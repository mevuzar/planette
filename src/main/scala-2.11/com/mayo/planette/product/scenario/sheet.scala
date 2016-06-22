package com.mayo.planette.product.scenario

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scalaz.Kleisli
import Kleisli._

/**
 * @author yoav @since 6/20/16.
 */
object sheet extends App {

  val system = ActorSystem()
  implicit val ctxt = system.dispatcher

  trait Deserializer[T]{
    def deserialize(any: Any): T
  }

  type ServerOperation[A,B] = Kleisli[Deserializer, ServerCommunication[A],  B]

  trait ServerCommunication[Request]{
    type  RawResponse

    def get(request: Request):RawResponse
  }

  trait ServerActorBasedCommunication[Request] extends ServerCommunication[Request]{
    override type RawResponse = Future[String]
  }

  case class SignUp(name: String, age: Int)

  case class UserCredentials(accountId: Int, token: java.util.UUID)

  trait Account {
    type SignUpRequest
    type SignInRequest
    type SignOutRequest
    type Credentials

    def signUp: ServerOperation[SignUpRequest, Credentials]

    def signIn: ServerOperation[SignInRequest, Credentials]

    def signOut: ServerOperation[SignOutRequest, Boolean]
  }

  trait UserAccountInterpreter extends Account{

    def deserializer[T]: Deserializer[T]
    def serverCommunication[T]: ServerCommunication[T]

    override type SignOutRequest = UserCredentials
    override def signUp: ServerOperation[SignUp, UserCredentials] = kleisli[Deserializer, ServerCommunication[SignUp], UserCredentials]{
      signUp =>{
        deserializer[UserCredentials]
      }
    }

    override def signIn: ServerOperation[UserCredentials, UserCredentials] = ???

    override def signOut: ServerOperation[UserCredentials, Boolean] = ???

    override type Credentials = UserCredentials
    override type SignInRequest = UserCredentials
    override type SignUpRequest = SignUp
  }


  object stam extends UserAccountInterpreter {
    override def deserializer[T]: Deserializer[T] = ???

    override def serverCommunication[T]: ServerCommunication[T] = ???


  }
}

class StringEchoActor extends Actor {
  override def receive: Receive = {
    case str: String => sender ! str

    case _ => println("we said a string duchy!!!")
  }
}
