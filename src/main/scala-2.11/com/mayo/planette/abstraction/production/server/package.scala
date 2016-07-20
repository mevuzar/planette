package com.mayo.planette.abstraction.production


/**
 * @author yoav @since 7/18/16.
 */
package object server {
  case class Lens[Object, Value](get: Object => Value, set: (Object, Value) => Object)

  def compose[Outer, Inner, Value](outer: Lens[Outer, Inner], inner: Lens[Inner, Value]) = {
    Lens[Outer, Value](
      get = outer.get andThen inner.get,
      set = (obj, value) => outer.set(obj, inner.set(outer.get(obj), value)))
  }
}
