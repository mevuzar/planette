package com.mayo.planette.product.domain

import scalaz.Kleisli

/**
 * @author yoav @since 6/17/16.
 */
package object client {
  trait CommunicationProtocol[Request, Response]{
    type Address
    def run(address: Address): Request => Response
  }

  type ServerOperation[Request, Response] = CommunicationProtocol[Request, Response] => CommunicationProtocol[Request, Response]#Address => Request => Response
}
