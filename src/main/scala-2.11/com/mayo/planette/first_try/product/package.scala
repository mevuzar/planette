package com.mayo.planette.first_try

/**
 * @author yoav @since 6/17/16.
 */
package object product {
  trait ScriptMocker {
    case class Stam(s:String)
    def mock[A]: A //Stam with A
    def mockWithAspects2[A,B]: A with B

  }

  trait MockTypeSwapper
}
