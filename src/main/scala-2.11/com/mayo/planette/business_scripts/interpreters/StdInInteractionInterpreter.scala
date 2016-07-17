package com.mayo.planette.business_scripts.interpreters

import java.time.{ZoneId, ZonedDateTime}

import com.mayo.planette.abstraction.terminology.client.{Interact, Question, PreDefInteractions}
import PreDefInteractions._
import scala.io.StdIn._
import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/16/16.
 */
object StdInInteractionInterpreter extends (Question ~> Id.Id){

  override def apply[A](fa: Question[A]): Id.Id[A] = fa match{
    case Interact(interaction) =>
      interaction match{
        case StringInteraction(q) => readLine(q + "\n")
        case DateInteraction(q) =>
          println(q + "\n")
          val year = readLine("year:\n").toInt
          val month = readLine("month:\n").toInt
          val day = readLine("day:\n").toInt
          val hours = readLine("hours:\n").toInt
          val minutes = readLine("minutes:\n").toInt
          val seconds = readLine("seconds:\n").toInt
          val date = ZonedDateTime.of(year, month, day, hours, minutes, seconds, 0, ZoneId.systemDefault())
          date

        case IntInteraction(q) =>
          println(q + "\n")
          readInt

        case DoubleInteraction(q) =>
          println(q + "\n")
          readDouble

        case UUIDInteraction(q) => java.util.UUID.fromString(readLine(q + "\n"))
      }


//    case InteractComplex(interactions: Seq[Interaction[_]]) =>
//      interactions.map(i => apply(Interact(i)))
  }
}
