package com.mayo.planette.product.scenario

import java.time.ZonedDateTime

import com.mayo.planette.domain.wishlists.model.{Cooking, PlanCategory, WishlistDSL}
import com.mayo.planette.product.ScriptMocker

import scala.util.{Success, Try}

/**
 * @author yoav @since 6/28/16.
 */
trait SignUpWishAndPlanCookingScenario extends ScriptMocker{

  val accountsService: ScriptServices.ScriptAccountService
  val wishlistsService: ScriptServices.ScriptWishlistsService[accountsService.AuthenticationToken]
  type EmptyQuestionnaire = wishlistsService.listItemQuestionnaireService.wishListDSLImpl.Questionnaire
  type WLDSL = wishlistsService.listItemQuestionnaireService.wishListDSLImpl.type
  val planningService: ScriptServices.ScriptPlanningService[accountsService.AuthenticationToken, WLDSL#AnsweredQuestionnaire]


  val signUpAndMakeAWish = for{
    token <- accountsService.signUp(mock[accountsService.SignUpRequest])
    questionnaire <- Try(wishlistsService.listItemQuestionnaireService.fillQuestionnaire(token)(mock[EmptyQuestionnaire]).get)
    wishlist <- wishlistsService.createWishlist(token)(mock[wishlistsService.WishlistCreate].addItemsToList(List(questionnaire)))
  }yield (token, wishlist)


  signUpAndMakeAWish match{
    case Success((token, wl)) =>
      val planQuestionnaire = planningService.serializationBridge.toB(wl.items.head)
      val ff = for{
        session <- planningService.plan(token)(planQuestionnaire)

      } yield session

      ff.get.questionnaireDSL
  }


}
