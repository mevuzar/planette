package com.mayo.planette.abstraction.terminology.common

/**
 * @author yoav @since 7/6/16.
 */
trait Serializer {
  def serialize[A,B]: A => B
  def deserialize[A,B]: A => B
}
