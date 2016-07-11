package com.mayo.planette.abstraction

/**
 * @author yoav @since 7/6/16.
 */
package object terminology {
  trait CommonOperations{
    type Operation[A,B]
  }

  trait AuthenticatedOperations{
    type AuthenticatedOperation[A,B]
  }
  
  trait CommonTerms{
    type AuthenticationToken
  }
}
