package com.mayo.planette.first_try.product
package scenario

import com.mayo.planette.first_try.domain.FA
import com.mayo.planette.first_try.product.scenario.ScriptServices._


import scala.util.{Success, Try}

/**
 * @author yoav @since 6/15/16.
 */
trait AccountScenarios extends ScriptMocker {

  val clientAccount: ScriptAccountService
  val userWishlists: ScriptWishlistsService //[clientAccount.AuthenticationToken]

  val mockSignUp = mock[clientAccount.SignUpRequest]
  //WithAspects2[clientAccount.SignUpRequest, userWishlists.AuthenticationToken]
  val signUpAndSignOut = for {
    credentials <- FA(clientAccount.signUp(mockSignUp))
    signOutResult <- FA(clientAccount.signOut(credentials))
  } yield (credentials, signOutResult)

  val signUpAndCreateWishlist = for {
    credentials <- FA(clientAccount.signUp(mockSignUp))
    wishlist <- FA(userWishlists.createWishlist(credentials)(mock[userWishlists.WishlistCreate]))
  } yield wishlist


  Try(signUpAndCreateWishlist.get) match {
    //TODO: use scalacheck here
    case Success(wishlist) => println("success")
    case _ => println("failure")
  }

  Try(signUpAndSignOut.get) match {
    //TODO: use scalacheck here
    case Success((token, _)) => {
      if (Try(FA(clientAccount.signOut(token)).get).isFailure) println("success")
      else println("failure")
    }

    case _ => println("failure")
  }
}


