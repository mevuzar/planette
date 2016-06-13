package fddd.book.chapter_six.streams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl._
import scala.collection.immutable.Seq
import scala.concurrent.Future
import OnlineService._
import Transaction.TransactionMonoid
import scalaz.{ Source => Sourcez, Sink => Sinkz, _ }
import Scalaz._
/**
 * Created by Owner on 6/13/2016.
 */
object App extends App{
  implicit val system = ActorSystem("net-transaction-graph")
  implicit val ctxt = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val transactionSource: Source[Transaction, NotUsed] = Source.fromFuture(allTransactions).mapConcat(identity)

  //transactionSource.runForeach(println)

  val grouped = transactionSource.map(validate).groupBy(Int.MaxValue, _.accountNo).fold(TransactionMonoid.zero)((t, trans) => TransactionMonoid.append(t, trans))
  val rightMaterialized = grouped.mergeSubstreams.toMat(Sink.foreach(println))(Keep.both)//.to(Sink.foreach(println)).run
  rightMaterialized.run()
}
