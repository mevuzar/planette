package fddd.book.chapter_six.ex_listing_6_7

import java.time.ZonedDateTime

import scala.concurrent.Future
import scalaz._
import Scalaz._
import Kleisli.kleisli
/**
 * @author yoav @since 6/8/16.
 */


object PreparedAndNonPreparedFutures {
  def pf1(i: Int): PFOperation = kleisli(i => {Future{
    Thread.sleep(i*1000)
    println(s"pf1 $Seq(i) time: ${ZonedDateTime.now.getSecond}")
    Seq(i)
  }})


  def pf2(i: Int): PFOperation = kleisli(i => {Future{
    Thread.sleep(i*1000)
    println(s"pf1 $Seq(i) time: ${ZonedDateTime.now.getSecond}")
    Seq(i)
  }})


  def pf3(i: Int): PFOperation = kleisli(i => {Future{
    Thread.sleep(i*1000)
    println(s"pf1 $Seq(i) time: ${ZonedDateTime.now.getSecond}")
    Seq(i)
  }})


  val a = pf1 (1).run(1)
  val b = pf2 (2).run(1)
  val c = pf3 (3).run(1)
  def preparedFuture(i: Int) = {
    println (ZonedDateTime.now.getSecond)
    val fff = for {
      aa <- a
      bb <- b
      cc <- c
    } yield (aa, bb, cc)
  }
}
