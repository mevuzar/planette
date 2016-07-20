package com.mayo.planette.abstraction.terminology.client

import com.mayo.planette.abstraction.terminology.client.UserInteractions.interact

import scalaz.{Free, Id, ~>}

/**
 * @author yoav @since 7/19/16.
 */
trait UserInteraction {
  def askUser[A](interaction: Interaction[A])(implicit interpreter: Question ~> Id.Id) = {
    val script = interact(interaction)
    val result = Free.runFC(script)(interpreter)
    result
  }
}
