package com.mayo.planette.abstraction.production.common

import com.mayo.planette.abstraction.terminology.client.{Answerable, Question, UserInteraction}


import scala.reflect.runtime.universe.{typeOf, TypeTag}
import scalaz.{Free, Id, ~>}

/**
 * @author yoav @since 7/6/16.
 */
trait UserInteractionProductionInterpreter extends UserInteraction{
  override type Operation[A,B] = A => B

  val interpreter: Question ~> Id.Id


  //override type Questions[A] = Free[Answerable, A]

  //override def getInfo[Info]: (Questions[_]) => Info = {questions => Free.runFC(questions)(interpreter)}
}
