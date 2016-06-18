package com.mayo.planette.product.domain

import scalaz.Kleisli

/**
 * @author yoav @since 6/17/16.
 */
package object client {
  trait CommunicationProtocol{
    type Address
    type RawResponse

    def run[Request](address: Address): Request => RawResponse
  }
  type ServerOperation[A,B] = CommunicationProtocol => A => B
}
