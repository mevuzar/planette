package com.mayo.planette.first_try.product.demo.cli_client.interpreters.accounts

import java.net.{InetAddress, InetSocketAddress}

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}
import com.mayo.planette.first_try.domain.Operations
import com.mayo.planette.first_try.product.client.domain.ClientAccountsService
import com.mayo.planette.first_try.product.demo.common.serialization.JsonSerializer
import com.typesafe.scalalogging.LazyLogging
import java.util.UUID
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.pickling.Defaults._, scala.pickling.json._
/**
 * @author yoav @since 7/4/16.
 */

trait AccountsService extends ClientAccountsService with JsonSerializer {

  case class AccessToken(token: UUID)
  object Opis extends Operations {
    override type AuthenticationToken = Future[AccessToken]
    //override type Operation[A, B] = A => B
  }

  override val operations = Opis
  import operations._


  override type SignUpRequest = model.SignUpRequest
  override type Credentials = model.Credentials

  override def signUp: Operation[SignUpRequest, AuthenticationToken] = { request => {
    Future.successful(AccessToken(UUID.randomUUID))
  }}
//
//    //    def source = Source[SignUpRequest](Seq(request).toList).map(x => {
//    //      //if(x % 1000 == 0)logger.info(s"source side: $x")
//    //      ByteString(x.toString)
//    //    }).via(new TCPBPClient(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 9000)).clientFlow)
//    //      .map(s => s)
//
//    //    source.runForeach(str => {
//    //      println(s"Client Got: $str")
//    //    })
//
//    Flow[String, AccessToken](List("").map(s =>AccessToken(UUID.fromString(s))))
//  }
//  }

  override def signIn: Operation[Credentials, Try[AuthenticationToken]] = ???

  override def signOut: Operation[AuthenticationToken, Boolean] = ???

}

object CommunicationExample extends App with LazyLogging {

  import akka.actor.ActorSystem
  import akka.stream.ActorMaterializer
  import akka.stream.scaladsl.{Flow, Sink, Tcp}
  import akka.util.ByteString
  import com.typesafe.scalalogging.LazyLogging


  class TCPBPClient(address: InetSocketAddress) extends LazyLogging {
    implicit val system = ActorSystem("stream")
    implicit val materializer = ActorMaterializer()
    implicit val ctxt = system.dispatcher


    val clientFlow = Tcp().outgoingConnection(address).map(s => {
      s.decodeString("UTF-8")
    })


  }

  class TCPBPServer(address: InetSocketAddress) extends LazyLogging {

    implicit val system = ActorSystem("stream")
    implicit val materializer = ActorMaterializer()
    implicit val ctxt = system.dispatcher

    val serverBinding = Tcp().bind(address.getHostString, address.getPort).to(Sink.foreach({ conn =>
      var count = 0
      val serverProtocol = conn.flow.join(Flow[ByteString].map(g => {
        try {
          count += 1
          def calcSleep = if (count > 3 && count < 5) {
            logger.info(s"server sleeps $count seconds")
            count * 1000
          } else {
            if (count >= 10) count = 0
            logger.info(s"server doesn't sleep")
            0
          }
          Thread.sleep(calcSleep)
          val countFromClient = Try {
            g.decodeString("UTF-8").split("#")(1).toInt
          } match {
            case Success(i) => i
            case Failure(e) => Int.MaxValue //this mod 1000 is not 0...
          }
          if (countFromClient % 1000 == 0) logger.info(s"server side payload size(chars): ${g.decodeString("UTF-8").size}")

          logger.info(s"server side payload size(chars): ${g.decodeString("UTF-8").size}")
          g
        }
        catch {
          case e: Exception =>
            logger.error("server exception: ", e)
            ByteString("server error")
        }
      }))
      serverProtocol.run
    })).run
  }


  implicit val system = ActorSystem("stream")
  implicit val materializer = ActorMaterializer()
  implicit val ctxt = system.dispatcher

  val ip = "127.0.0.1"
  val port = 9000
  val server = new TCPBPServer(new InetSocketAddress(ip, port))
  server.serverBinding onSuccess {
    case address => logger.info("server connected.")

    case _ =>
  }


  def source = Source(1 to 10000000).map(x => {
    //if(x % 1000 == 0)logger.info(s"source side: $x")
    ByteString(s"Message #$x")
  }).via(new TCPBPClient(new InetSocketAddress(InetAddress.getByName(ip), port)).clientFlow)
    .map(s => s)

  source.runForeach(str => {
    println(s"Client Got: $str")
  })


}