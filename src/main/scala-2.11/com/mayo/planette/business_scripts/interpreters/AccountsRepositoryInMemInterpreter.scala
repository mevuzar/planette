package com.mayo.planette.business_scripts.interpreters

import java.util.UUID

import com.mayo.planette.abstraction.production.common.model.AccountModel
import AccountModel._
import com.mayo.planette.abstraction.production.server.accounts.dal.AccountRepositoryF.DSL._
import com.mayo.planette.abstraction.terminology.DataDSL.{DataOpteration, DataStoreRequest}

import scala.collection.mutable.HashMap
import scala.util.{Right, Try}
import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/10/16.
 */
object AccountsRepositoryInMemInterpreter extends (DataStoreRequest ~> Id.Id) {

  import Id._

  val map = HashMap.empty[UUID, (UserToken, UserAccount)]

  def apply[A](in: DataStoreRequest[A]): Id[A] =
    in match {
      //case Pure(a) => a
      case DataOpteration(operation) =>
        operation match {
          case AccountQuery(userId) =>

            Right(Try {
              map.find(a => a._1 == userId).get._2._2
            })

          case StoreAccount(account) =>
            val tryIt = Try {
              val token = UUID.randomUUID //.toString
              map.put(account.id, (UserToken(account.id, token), account))
              ()
            }

            Right(tryIt)

          case DeleteAccount(userId) =>
            Right(Try {
              map.remove(userId)
              ()
            })
        }
    }
}



