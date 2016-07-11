package com.mayo.planette.abstraction.production
package server

import com.mayo.planette.abstraction.terminology.server.AccountsRepository
import scala.collection.mutable.HashMap
import java.util.UUID
import common.accounts.model.AccountModel._

import scala.concurrent.Future
import scalaz.concurrent.Task
import Task._

/**
 * @author yoav @since 7/10/16.
 */
trait AccountRepositoryInMemInterpreter extends AccountsRepository[UserAccount, UUID]{

  val map = HashMap.empty[UUID, (UUID, UserAccount)]

//  def interprete[A](accountAction: AccountRepoF[A]):Future[AccountRepoF[A]] = {
//    accountAction match{
//      case Store(account, next) =>
//        val token = UUID.randomUUID
//        map.put(account.uuid, (token, account))
//        Future((token)) //.join(token)
//
//      case Query(no, onResult) => Future(map(no))
//      case Delete(no, next) => Future(())
//    }
//  }
}
