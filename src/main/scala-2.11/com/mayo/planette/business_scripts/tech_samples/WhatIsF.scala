package com.mayo.planette.business_scripts.tech_samples

import com.mayo.planette.business_scripts.tech_samples.CommandFExample.NameQuestion
import com.mayo.planette.business_scripts.tech_samples.WhatIfDSL.NameProvision
import com.mayo.planette.business_scripts.tech_samples.WhatIsF.{Interact, Info, Interaction}
import com.mayo.whatis.mean.{Action, Meaning, Person, Present, Someone, Thing, Time}

import scalaz.{~>, Id, Coyoneda, Free}

/**
 * @author yoav @since 7/15/16.
 */
object WhatIsF {

  sealed trait Info[+A]

  type Providable[A] = Coyoneda[Info, A]

  sealed trait Interaction[+A]

  final case class Interact[A](interaction: Interaction[A]) extends Info[A]

  object Interactions {
    def interact[A](interaction: Interaction[A]): Free[Providable, A] = {
      Free.liftFC(Interact(interaction): Info[A])
    }
  }

}

object WhatIfDSL {

  case object Anytime extends Time

  trait Command extends Meaning {
    val action: Action
    val carrier: Person
    val when: Time
  }

  case object ApplicationUser extends Someone

  trait InformationProvision extends Action {
    override type Performer = Person
    val what: Thing
    override val performer = Someone
  }

  trait Name extends Thing

  case object Name extends Name

  case object NameProvision extends InformationProvision {
    override val what = Name
    override val when = Anytime

  }

  trait InfoQuestion extends Command {
    override val action: InformationProvision
  }

  object EnterNameCommand extends InfoQuestion {
    override val when: Time = Present
    override val action: InformationProvision = NameProvision
    override val carrier: Person = ApplicationUser
  }

}

object StdInInteractionInterpreter extends (Info ~> Id.Id) {
//  override def apply(v1: Info): Id.Id = v1 match {
//    case Interact(interaction) => interaction
//  }
  override def apply[A](fa: Info[A]): Id.Id[A] = {
  fa match {
        case Interact(interaction) => interaction match{
          case NameQuestion(command) =>
            val prompt = s"Hi ${command.carrier.name}, please"
            command.action match {
              case NameProvision =>
                io.StdIn.readLine(s"$prompt enter your name\n")
            }
        }
  }
}
}

object CommandFExample extends App{

  import WhatIfDSL._
  import WhatIsF.Interactions.interact

  case class NameQuestion(command: Command) extends Interaction[String]
  val nameQuestionScript = interact(NameQuestion(EnterNameCommand))

  val scriptResult = Free.runFC(nameQuestionScript)(StdInInteractionInterpreter)

  println(s"result: $scriptResult")
}
