package com.mayo.planette.product
package scenario

import com.mayo.planette.domain.ServerOperations
import com.mayo.planette.product.client.domain.{ClientAccountsService, ClientWishlistsService}

import scala.util.{Success, Try}
import ScriptServices._

/**
 * @author yoav @since 6/15/16.
 */
trait AccountScenarios extends ScriptMocker {

  val clientAccount: ScriptAccountService
  val userWishlists: ScriptWishlistsService//[clientAccount.AuthenticationToken]

  val mockSignUp = mock[clientAccount.SignUpRequest]//WithAspects2[clientAccount.SignUpRequest, userWishlists.AuthenticationToken]
  val signUpAndSignOut = for {
    credentials <- clientAccount.signUp(mockSignUp)
    signOutResult <- clientAccount.signOut(credentials)
  } yield (credentials, signOutResult)

  val signUpAndCreateWishlist = for {
    credentials <- clientAccount.signUp(mockSignUp)
    wishlist <- userWishlists.createWishlist(credentials)(mock[userWishlists.WishlistCreate])
  } yield wishlist


  Try(signUpAndCreateWishlist.get) match {
    //TODO: use scalacheck here
    case Success(wishlist) => println("success")
    case _ => println("failure")
  }

  Try(signUpAndSignOut.get) match {
    //TODO: use scalacheck here
    case Success((token, _)) => {
      if (Try(clientAccount.signOut(token).get).isFailure) println("success")
      else println("failure")
    }

    case _ => println("failure")
  }
}


