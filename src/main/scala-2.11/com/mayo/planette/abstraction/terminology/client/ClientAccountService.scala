package com.mayo.planette.abstraction.terminology.client

import com.mayo.planette.abstraction.terminology.common.AccountsService

/**
 * @author yoav @since 7/6/16.
 */
private[abstraction] trait ClientAccountService extends AccountsService{
  def localToken: Option[AuthenticationToken]
  type LocalDataStore
  val dataStore: LocalDataStore
  def storeLocalData[Data](name: String, data: Data): LocalDataStore
}
