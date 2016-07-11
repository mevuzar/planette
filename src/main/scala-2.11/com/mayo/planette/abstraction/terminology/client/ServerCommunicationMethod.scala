package com.mayo.planette.abstraction.terminology.client

/**
 * @author yoav @since 7/6/16.
 */
private[abstraction] trait ServerCommunicationMethod {
  type AddressType
  type AddressConfiguration
  type ResponseType
  type ServerCaller[A]
}
