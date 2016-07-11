package com.mayo.planette.abstraction.terminology.client

import com.mayo.planette.abstraction.terminology.common.AccountsService

/**
 * @author yoav @since 7/6/16.
 */
private[abstraction] trait ClientAccountService extends AccountsService{
  def localToken: AuthenticationToken
}