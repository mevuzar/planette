package com.mayo.planette.first_try.product.client
package domain

import com.mayo.planette.first_try.domain.accounts.service.AccountsService
import com.mayo.planette.first_try.product.domain.client.ServerConnection

import scala.util.Try
import scalaz.Monad


/**
 * @author yoav @since 6/17/16.
 */
trait ClientAccountsService extends AccountsService{

  val serverConnection: ServerConnection
}
