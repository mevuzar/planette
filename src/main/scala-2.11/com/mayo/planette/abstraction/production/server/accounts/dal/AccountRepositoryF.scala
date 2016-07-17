package com.mayo.planette.abstraction.production.server.accounts.dal

import com.mayo.planette.abstraction.production.common.accounts.model.AccountModel._
import com.mayo.planette.abstraction.production.server.accounts.dal.interpreters.AccountsRepositoryLoggerInterpreter
import com.mayo.planette.abstraction.terminology.DataDSL.{DataCall, DataOperations}
import com.mayo.planette.abstraction.terminology.StringOr
import com.mayo.planette.business_scripts.interpreters.AccountsRepositoryInMemInterpreter

import scalaz.Free

/**
 * @author yoav @since 7/11/16.
 */
object AccountRepositoryF {

  import java.util.UUID

  import scala.util.Try
  import scalaz.Free

  /**
   * @author yoav @since 7/10/16.
   */


  object DSL {

    type FreeCall[A] = Free[DataCall, A]
    sealed trait AccountDataCall[+A] extends DataCall[A]
    final case class AccountQuery(userId: UUID) extends AccountDataCall[StringOr[Try[UserAccount]]]

    final case class StoreAccount(account: UserAccount) extends AccountDataCall[StringOr[Try[Unit]]]

    final case class DeleteAccount(userId: UUID) extends AccountDataCall[StringOr[Try[Unit]]]

  }

  object AccountDataOperations extends DataOperations{
    import DSL._
    
    def query(id: UUID) = dataOperation(AccountQuery(id))

    def store[A](userAccount: UserAccount) = dataOperation(StoreAccount(userAccount))

    def delete[A](id: UUID) = dataOperation(DeleteAccount(id))

  }
}


