package com.mayo.planette.first_try.product.demo.server.accounts.app

import java.net.InetSocketAddress

import scala.util.{Failure, Success, Try}

/**
 * @author yoav @since 7/4/16.
 */
object Boot extends App{
  import akka.actor.ActorSystem
  import akka.stream.ActorMaterializer
  import akka.stream.scaladsl.{Tcp, Sink, Flow}
  import akka.util.ByteString
  import com.typesafe.scalalogging.LazyLogging

  class TCPBPServer(address: InetSocketAddress) extends LazyLogging{

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
            if(count >= 10)count = 0
            logger.info(s"server doesn't sleep")
            0
          }
          Thread.sleep(calcSleep)
          val countFromClient = Try{g.decodeString("UTF-8").split("#")(1).toInt} match {
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
}
