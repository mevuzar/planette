package com.mayo.planette.first_try.product.scenario

import java.time.ZonedDateTime

import com.mayo.planette.first_try.domain.{FA, Serialized}
import com.mayo.planette.first_try.domain.wishlists.model.{Cooking, PlanCategory, WishlistDSL}
import com.mayo.planette.first_try.product.ScriptMocker

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
      token <- FA(accountsService.signUp(mock[accountsService.SignUpRequest]))
      questionnaire <- FA(wishlistsService.listItemQuestionnaireService.fillQuestionnaire(token)(mock[EmptyQuestionnaire]))
      wishlist <- FA(wishlistsService.createWishlist(token)(mock[wishlistsService.WishlistCreate].addItemsToList(List(questionnaire.get))))
    } yield (token, wishlist)


    Try(signUpAndMakeAWish.get) match {
      case Success((token, wl)) =>
        val serializableWishlistItem = Serialized(wl.items.head)
        val planWishlistItem = planningService.serializationBridge.toB(serializableWishlistItem)
        val plan = for {
          plan <- planningService.plan(token)(wl.items.head)
        } yield plan

        println(s"success! plan: $plan")
        Success((token, plan))

      case _ =>
        println("failure")
        Failure(new Exception("failed to sign-up and plan cooking"))
    }
  }





}
