package com.mayo.planette.product.scenario

import com.mayo.planette.Tryable
import com.mayo.planette.product.ScriptMocker
import ScriptServices.{ScriptAccountService, ScriptWishlistsService}

import scala.util.{Try, Failure, Success}

/**
 * @author yoav @since 6/22/16.
 */
trait WishlistScenarios extends ScriptMocker{

  val account: ScriptAccountService
  val wishlists: ScriptWishlistsService[account.AuthenticationToken]

  val token = mock[account.AuthenticationToken]

    def createWishlistWithItems:Try[wishlists.Wishlist] = {
      (for {
        q1 <- wishlists.userFillQuestionaire()
        q2 <- wishlists.userFillQuestionaire()
        q3 <- wishlists.userFillQuestionaire()
        createRequest <- Success(mock[wishlists.WishlistCreate].addItemsToList(List(q1, q2, q3)))
        createWishlist <- wishlists.createWishlist(token)(createRequest)
      } yield (createWishlist)) match {
        case Success(wishlist) if wishlist.items.size == 3 =>
          println("success")
          Success(wishlist)
        case _ => Failure(new Exception("failed to createWishlistWithItems"))
      }
    }

    def createWishlistItemsUsingPlanQuestionnaireService: Try[List[wishlists.WishlistItem]] = {
      val questionnaire1 = mock[wishlists.listItemQuestionnaireService.wishListDSLImpl.Questionnaire]
      val questionnaire2 = mock[wishlists.listItemQuestionnaireService.wishListDSLImpl.Questionnaire]
      val questionnaire3 = mock[wishlists.listItemQuestionnaireService.wishListDSLImpl.Questionnaire]

      val answeredQuestionnaires = for {
           answer1 <- wishlists.listItemQuestionnaireService.fillQuestionnaire(token)(questionnaire1)
           answer2 <- wishlists.listItemQuestionnaireService.fillQuestionnaire(token)(questionnaire2)
           answer3 <- wishlists.listItemQuestionnaireService.fillQuestionnaire(token)(questionnaire3)
      } yield(List(answer1, answer2, answer3))

      val items = Try(answeredQuestionnaires.get)
      items
    }

    def updateWishlist(wishlist: wishlists.Wishlist): Try[wishlists.Wishlist] = {
      val updateRequest = mock[wishlists.WishlistUpdate].addItemsToRemoveList(wishlist.items.filter(_.periodic))
      val questionnaires = Range(0,4).map(_ => wishlists.userFillQuestionaire()).toList
      val createdItems = questionnaires.map(t => t.get)
      val newUpdateRequest = updateRequest.addItemsToAddList(createdItems)
      wishlists.updateWishlist(token)(newUpdateRequest)
    }
  }



