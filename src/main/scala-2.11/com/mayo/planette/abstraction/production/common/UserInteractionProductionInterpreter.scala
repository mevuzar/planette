package com.mayo.planette.abstraction.production.common

import com.mayo.planette.abstraction.terminology.client.UserInteraction

/**
 * @author yoav @since 7/6/16.
 */
trait UserInteractionProductionInterpreter extends UserInteraction{
  override type Operation[A,B] = A => B
  override type QuestionType = String
}
