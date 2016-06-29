package fddd.book.chapter_five.free_monads

import com.mayo.planette.domain.wishlists.model.{PlanCategory, PlanType}

import scalaz.Free._
import scalaz.Functor


/**
 * @author yoav @since 6/25/16.
 */
trait PlanQuestionnaireBuilder {

  def setPlanType(planType: PlanType): PQB[Unit] = liftF(SetPlanType(planType, ()))

  def setPlanCategory(planCategory: PlanCategory): PQB[Unit] = {
    liftF(SetPlanCategory(planCategory, ()))
  }

  def setPeriodic(periodic: Boolean): PQB[Unit] = {
    liftF(SetPeriodic(periodic, ()))
  }
}

sealed trait PlanQuestionnaireBuilderF[+A]

final case class SetPlanType[+A](planType: PlanType, next: A) extends PlanQuestionnaireBuilderF[A]

final case class SetPeriodic[+A](periodic: Boolean, next: A) extends PlanQuestionnaireBuilderF[A]

final case class SetPlanCategory[+A](planCategory: PlanCategory, next: A) extends PlanQuestionnaireBuilderF[A]

object PlanQuestionnaireBuilderF {
  implicit val functor: Functor[PlanQuestionnaireBuilderF] = new Functor[PlanQuestionnaireBuilderF] {
    override def map[A, B](fa: PlanQuestionnaireBuilderF[A])(f: (A) => B): PlanQuestionnaireBuilderF[B] = fa match {
      case SetPlanType(planType, next) => SetPlanType(planType, f(next))
      case SetPeriodic(periodic, next) => SetPeriodic(periodic, f(next))
      case SetPlanCategory(planCategory, next) => SetPlanCategory(planCategory, f(next))
    }
  }
}


