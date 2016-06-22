package com.mayo.planette.product.client
package domain

import com.mayo.planette.domain.accounts.service.AccountsService
import com.mayo.planette.product.domain.client.ServerConnection

import scala.util.Try
import scalaz.Monad


/**
 * @author yoav @since 6/17/16.
 */
trait Account extends AccountsService{

  val serverConnection: ServerConnection
}
