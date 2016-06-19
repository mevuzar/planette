package com.mayo.planette.product.domain.client.domain

import akka.stream.scaladsl.Source

import scalaz.Kleisli

/**
 * @author yoav @since 6/18/16.

 * There are two ways to extract info from the user:
 * 1. QuestionAndAnswer e.g a form or just a simple question to which there's a
 *    single answer
 * 2. Questionnaire, where multiple details are needed to complete the answer and where
 *    the question list is not deterministic - an certain answer to question x may raise
 *    a question y.
 */

trait UserInteraction {

  type Question
  type Answer

  type Questionnaire = Kleisli[List, Source[Question, Unit], Answer] with UserInfoRequest[Answer]
  type QuestionAndAnswer = Question => Answer with UserInfoRequest[Answer]

  trait UserInfoRequest[Info]{

    def getInfo: Info
  }

}
