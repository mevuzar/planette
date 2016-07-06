package com.mayo.planette.first_try.product.demo.common.serialization

import org.json4s.JsonAST.JValue

/**
 * @author yoav @since 2/20/16.
 */
trait DeserializationTransformer {
  def transformDeserialized(value: JValue): JValue = value
}
