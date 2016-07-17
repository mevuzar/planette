package com.mayo.planette.abstraction.terminology
package client

/**
 * @author yoav @since 6/18/16.

 *         There are two ways to extract info from the user:
 *         1. QuestionAndAnswer e.g a form or just a simple question to which there's a
 *         single answer
 *         2. Questionnaire, where multiple details are needed to complete the answer and where
 *         the question list is not deterministic - an certain answer to question x may raise
 *         a question y.
 */

private[abstraction] trait UserInteraction extends CommonOperations {

  type Questions

  def getInfo[Info]: Operation[Questions, Info]

}

