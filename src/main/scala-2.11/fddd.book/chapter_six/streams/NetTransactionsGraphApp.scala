package fddd.book.chapter_six.streams

import akka.{Done, NotUsed}
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
object NetTransactionsGraphApp extends App{

  implicit val system = ActorSystem("net-transaction-graph")
  implicit val ctxt = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val accountNos: Source[String, NotUsed] = Source.fromFuture(allAccounts).mapConcat(identity)
  val accounts = Flow[String].map(queryAccount(_, AccountRepository))

  val getBnkTrans = Flow[Account].mapConcat(getBankingTransactions)
  val getSettleTrans = Flow[Account].mapConcat(getSettlementTransactions)
  val validateTrans = Flow[Transaction].map(validate)

  val netTxns: Sink[Transaction, Future[Map[String, Transaction]]] = Sink.fold[Map[String, Transaction], Transaction](Map.empty[String, Transaction])({(t, t1)=>
  t |+| Map(t1.accountNo -> t1)
  }).mapMaterializedValue(f => {
    f onSuccess{
      case map => println(s"netTxns: ${map.mkString("\n")}")
    }
    f
  })

  val audit: Sink[Transaction, Future[Done]] = Sink.foreach((tr: Transaction) => println(s"audit: $tr"))

  RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._
    val in = accountNos
    val out = Sink.ignore

    val bcastAccounts = builder.add(Broadcast[Account](2))
    val mergeTransactions = builder.add(Merge[Transaction](2))
    val bcastNetTransaction = builder.add(Broadcast[Transaction](2))

    in ~> accounts ~> bcastAccounts ~> getBnkTrans ~> mergeTransactions ~> validateTrans ~> bcastNetTransaction ~> netTxns

    bcastAccounts ~> getSettleTrans ~> mergeTransactions

    bcastNetTransaction ~> audit

    ClosedShape
  }).run

}
