package com.mayo.planette

/**
 * @author yoav @since 6/17/16.
 */
package object product {
  trait ScriptMocker {
    def mock[A]: A

    def mockWithAspects2[A,B]: A with B

  }

  trait MockTypeSwapper
}
