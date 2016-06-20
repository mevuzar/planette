package com.mayo.planette.product.scenario

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future

/**
 * @author yoav @since 6/20/16.
 */
object sheet extends App {

  val system = ActorSystem()
  implicit val ctxt = system.dispatcher

  trait CommunicationEffect[Address, Request] {
    type RawResponse
    //type Address

    def run: Address => Request => RawResponse
  }

  type Address = CommunicationEffect[Address, _]
  type CoOp[A] = CommunicationEffect[Address, A] => Address => A => CommunicationEffect[_, _]#RawResponse

  object CoOp {
    def apply[A]: CoOp[A] = { communication => {
      address =>
        communication.run(address)
    }
      
    }
  }

  object CommunicationEffecti extends CommunicationEffect[ActorRef, String] {
    override type RawResponse = Future[String]

    //override type Address = ActorRef

    override def run: (ActorRef) => (String) => Future[String] = { actor => { request =>
      implicit val timeout = Timeout(5, TimeUnit.SECONDS)
      (actor ? request).map(_.toString)
    }
    }
  }


  def concrete = CoOp[

  , String]
  concrete
  val effct = CommunicationEffecti
  //  val next = concrete(effct)
  //  next
  val response = concrete[](effct)(system.actorOf(Props[StringEchoActor]))("string string")

  println(s"response: $response")
}

class StringEchoActor extends Actor {
  override def receive: Receive = {
    case str: String => sender ! str

    case _ => println("we said a string duchy!!!")
  }
}
