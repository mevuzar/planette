package com.mayo.planette.abstraction.production

import scalaz.Free

/**
 * @author yoav @since 7/6/16.
 */
package object client {
  type FreeCall[A] = Free[ServerCallF, A]
}
