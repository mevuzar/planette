package fddd.book.chapter_five.free_monads

import com.mayo.planette.domain.wishlists.model.{Cooking, PlanTypeGoal}

/**
 * @author yoav @since 6/26/16.
 */
object FreeQuesionnaireGeneratorTestApp extends App {

  object quesionnaireBuilder extends PlanQuestionnaireBuilder

  val createQ = for {
    _ <- quesionnaireBuilder.setPeriodic(true)
    _ <- quesionnaireBuilder.setPlanCategory(Cooking)
    res <- quesionnaireBuilder.setPlanType(PlanTypeGoal)
  } yield res

  val v = ShowInterpreter.interpret(createQ, List.empty[String])
  println(v)


}

object ShowInterpreter {

  def interpret[A](script: PQB[A], ls: List[String]): List[String] = script.fold(_ => ls, {

    case SetPlanType(planType, next) =>
      interpret(next, ls ++ List(s"SetPlanType: $planType"))

    case SetPeriodic(periodic, next) =>
      interpret(next, ls ++ List(s"SetPeriodic $periodic"))

    case SetPlanCategory(planCategory, next) =>
      interpret(next, ls ++ List(s"SetPlanCategory $planCategory"))

  })

}

