package com.mayo.planette.abstraction.production.client.wishlists

import com.mayo.planette.abstraction.production.common.model.PlaningModel.{ActivityWishType, AssignmentWishType, GoalWishType, WishType}
import com.mayo.planette.abstraction.production.common.model.WishlistsModel.Wish
import com.mayo.planette.abstraction.terminology.client.PreDefInteractions._
import com.mayo.planette.abstraction.terminology.client.{Interaction, Question}
import com.mayo.planette.abstraction.terminology.client.UserInteractions.interact

import scala.util.{Failure, Success}
import scalaz.{Free, Id, ~>}

/**
 * @author yoav @since 7/19/16.
 */
trait WishlistsUserInteractions {

  def askForWish(interpreter: Question ~> Id.Id) = {
    val script = for{
      description <- askDescription
      category <- askCategory
      wishType <- askType
    } yield (wishType, category, description)

    val interpreted = Free.runFC(script)(interpreter)

    Wish.generateWish _ tupled(interpreted)
  }
  
  def askDescription = interact(StringInteraction("please describe your wish"))

  def askCategory = interact(StringInteraction("in your opinion, what is the category your wish belongs to?"))

  def askType = {
    val wishTypeInteraction = StringBasedTransformation[WishType]("which type of wish is it(assignment|activity|goal)?", {s =>
      s.toLowerCase match{

        case "goal" | "g" => Success(GoalWishType)
        case "assignment" | "as" => Success(AssignmentWishType)
        case "activity" | "ac" => Success(ActivityWishType)
        case other => Failure(InteractionException(other, List("goal", "g", "assignment", "as", "activity", "ac")))
      }})
    interact(wishTypeInteraction)
  }
  
  case class InteractionException(input: String, valid: List[String]) extends Exception(s"wrong input: $input, expected any of ${valid.mkString("|")}")
}
