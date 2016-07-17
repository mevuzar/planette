package com.mayo.planette.business_scripts.tech_samples

import scala.reflect.runtime.universe.{TypeTag, typeOf}

/**
 * @author yoav @since 7/17/16.
 */
object TypeTagMembersExtractionSample extends App{
  def getInfo[Info: TypeTag] = {
        val args = typeOf[Info].decls.filter(p => p.isMethod == false)
        val ff = args.map { arg =>
          val t = arg.info
          type A = t.type

          arg.name
          //val input = scala.io.StdIn.readLine(s"please enter ${arg.fullName.split('.').last}\n")
          //input.asInstanceOf[A] //Todo: How can we extract the type (t.type isn't the right type, it's "scala.reflect.api.Types$TypeApi")?
        }
      println(args)
      }

  case class Person(name: String, age: Int)
  getInfo[Person]
}
