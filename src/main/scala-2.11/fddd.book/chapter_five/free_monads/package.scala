package fddd.book.chapter_five

import scalaz.Free

/**
 * @author yoav @since 6/27/16.
 */
package object free_monads {
  type PQB[A] = Free[PlanQuestionnaireBuilderF, A]
}
