package com.mayo.planette.domain.planning.service

import scalaz.Functor

/**
 * @author yoav @since 6/28/16.
 */
trait QuestionsF {

}

trait PlanQuestionnaireBuilder {


}

sealed trait Question[+A]


//object Question {
//  implicit val functor: Functor[Question] = new Functor[Question] {
//    override def map[A, B](fa: Question[A])(f: (A) => B): Question[B] = fa match {
//
//    }
//  }
//}

