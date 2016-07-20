package com.mayo.planette.abstraction.terminology.client

import com.mayo.planette.abstraction.terminology.common.AccountsService

import scala.util.Try
import scalaz.Reader

/**
 * @author yoav @since 7/6/16.
 */
private[abstraction] trait ClientAccountService extends AccountsService{
  val dataStore: AccountLocalRepository[Account]

  //def getAccount: Reader[AccountLocalRepository[Account], Account]
}

trait AccountLocalRepository[Account]{
  def getAccount: Option[Account]
  def storeAccount(account: Account): Try[Unit]
  def updateAccount(f: Account => Account): Try[Unit]

  case object AccountDoesNotExistException extends Exception("account doesn't exist")
}