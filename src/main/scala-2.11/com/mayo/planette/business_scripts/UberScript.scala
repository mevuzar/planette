package com.mayo.planette.business_scripts

import com.mayo.planette.abstraction.production.common.model.AccountModel.{UserAccount, UserToken}
import com.mayo.planette.abstraction.terminology.client.AccountLocalRepository
import com.mayo.planette.business_scripts.interpreters.{UserAccounts, WishlistsRepositoryInMemInterpreter, StdInInteractionInterpreter}

import scala.concurrent.Await
import scala.concurrent.duration._
import scalaz.Reader

/**
 * @author yoav @since 7/20/16.
 */
object UberScript extends App {

  val account = Await.result(SignUpScript.userInputToSignUpRequest(StdInInteractionInterpreter), 5 seconds).get
  val environment = new Environment {
    override def getToken: Reader[AccountLocalRepository[UserAccount], UserToken] = Reader { repo =>
      val account = repo.getAccount.get
      UserToken(account.id, account.token)
    }
  }
  val createWishlist = WishlistsScript.createWishlist(environment, StdInInteractionInterpreter)
  createWishlist.run(UserAccounts.dataStore)

  println(WishlistsRepositoryInMemInterpreter.map)
}

trait Environment {
  def getToken: Reader[AccountLocalRepository[UserAccount], UserToken]
}