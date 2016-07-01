package com.mayo.planette.product.scenario

import java.time.ZonedDateTime

import com.mayo.planette.domain.Serialized
import com.mayo.planette.domain.wishlists.model.{Cooking, PlanCategory, WishlistDSL}
import com.mayo.planette.product.ScriptMocker

import scala.util.{Failure, Success, Try}

/**
 * @author yoav @since 6/28/16.
 */
trait SignUpWishAndPlanCookingScenario extends ScriptMocker{



  val accountsService: ScriptServices.ScriptAccountService
  val wishlistsService: ScriptServices.ScriptWishlistsService
  type EmptyQuestionnaire = wishlistsService.listItemQuestionnaireService.wishListDSLImpl.Questionnaire
  type WLDSL = wishlistsService.listItemQuestionnaireService.wishListDSLImpl.type
  val planningService: ScriptServices.ScriptPlanningService[wishlistsService.WishlistItem] //WLDSL#AnsweredQuestionnaire]

  def signUpWishAndPlanCookingScenario = {
    val signUpAndMakeAWish = for {
      token <- accountsService.signUp(mock[accountsService.SignUpRequest])
      questionnaire <- wishlistsService.listItemQuestionnaireService.fillQuestionnaire(token)(mock[EmptyQuestionnaire])
      wishlist <- wishlistsService.createWishlist(token)(mock[wishlistsService.WishlistCreate].addItemsToList(List(questionnaire)))
    } yield (token, wishlist)


    Try(signUpAndMakeAWish.get) match {
      case Success((token, wl)) =>
        val serializableWishlistItem = Serialized(wl.items.head)
        val planWishlistItem = planningService.serializationBridge.toB(serializableWishlistItem)
        val plan = for {
          session <- planningService.plan(token)(wl.items.head)
          fPlan <- session
        } yield fPlan

        println(s"success! plan: $plan")
        Success((token, plan))

      case _ =>
        println("failure")
        Failure(new Exception("failed to sign-up and plan cooking"))
    }
  }





}
