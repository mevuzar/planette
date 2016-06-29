package com.mayo.planette.domain.planning

import scalaz.Free

/**
 * @author yoav @since 6/25/16.
 */
package object service {
  type FreeQuestions[A] = Free[Question, A]
}
