package com.mayo.planette.abstraction.production.server.accounts.dal.interpreters

import com.mayo.planette.abstraction.production.server.accounts.dal.AccountRepositoryF.DSL._
import com.mayo.planette.abstraction.terminology.DataDSL.{DataOpteration, DataStoreRequest}

import scalaz.Id.Id
import scalaz.{Id, ~>}

/**
 * @author yoav @since 7/11/16.
 */
object AccountsRepositoryLoggerInterpreter extends (DataStoreRequest ~> Id.Id) {
  def apply[A](in: DataStoreRequest[A]): Id[A] =
    in match {
      case DataOpteration(op) =>
        op match {
          case AccountQuery(userId) =>

            Left(s"Getting account for user id: $userId")


          case StoreAccount(account) =>
            Left(s"Storing user account $account")

          case DeleteAccount(userId) =>

            Left(s"Deleting user account with id: $userId")

        }
    }
}
