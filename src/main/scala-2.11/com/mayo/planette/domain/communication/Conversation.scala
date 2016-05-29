package com.mayo.planette.domain.communication

/**
 * @author yoav @since 5/9/16.
 */
trait Conversation {
  type Question
  type Answer

  val questions: Seq[Question]
  val answers: Seq[Answer]

  protected def ask(question: Question): Answer

}
