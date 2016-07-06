package com.mayo.planette.first_try.product.demo

import com.mayo.planette.first_try.domain.Operations

/**
 * @author yoav @since 7/5/16.
 */
package object server {
  object ServerOperations extends Operations{
    override type AuthenticationToken = String
  }
}
