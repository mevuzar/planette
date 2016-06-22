package com.mayo.planette.product
package scenario

import com.mayo.planette.product.client.domain.{Account, UserWishlists}

import scala.util.{Success, Try}

/**
 * @author yoav @since 6/15/16.
 */
trait SignupScript extends ScriptMocker {

  val clientAccount: ScriptAccount
  val userWishlists: ScriptUserWishlists[clientAccount.AuthenticationToken]

  val mockSignUp = mockWithAspects2[clientAccount.SignUpRequest, userWishlists.AuthenticationToken]
  val signUpAndSignOut = for {
    credentials <- clientAccount.signUp(mockSignUp)
    signOutResult <- clientAccount.signOut(credentials)
  } yield (credentials, signOutResult)

  val signUpAndCreateWishlist = for {
    credentials <- clientAccount.signUp(mockSignUp)
    wishlist <- userWishlists.createWishlist(credentials)(mock[userWishlists.WishlistCreate])
  } yield wishlist


  signUpAndCreateWishlist match {
    //TODO: use scalacheck here
    case Success(wishlist) => println("success")
    case _ => println("failure")
  }

  signUpAndSignOut match {
    //TODO: use scalacheck here
    case Success((token, _)) => {
      if (clientAccount.signOut(token).isFailure) println("success")
      else println("failure")
    }

    case _ => println("failure")
  }
}

trait ScriptAccount extends Account {
  override type Operation[A, B] = A => Try[B]
}

trait ScriptUserWishlists[AccountsToken] extends UserWishlists {
  override type AuthenticationToken = AccountsToken
  override type Operation[A, B] = A => Try[B]
}

