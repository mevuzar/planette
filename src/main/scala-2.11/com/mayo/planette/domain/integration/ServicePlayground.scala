package com.mayo.planette.domain
package integration


import com.mayo.planette.domain.languages.TranslatorService
import com.mayo.planette.domain.planning.PlannerService
import com.mayo.planette.domain.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ServicePlayground {

  val userService: UserService
  val plannerService: PlannerService
  val translatorService: TranslatorService
  val userClient: UserClient


  import plannerService.{PlanRequest, PlanningSession}
  import userService.{Family, User, UserSignupDetails}

  val family: Family

  def positiveRequestFlow: Future[PlanningSession] = {

    val planRequest = userClient.send[PlanRequest]
    val futurePlan = plannerService.requestPlan(planRequest) map {
      case Some(plan) => plannerService.startPlanning(plan)
      case None => Future.failed(new Exception(s"there is no plan for request $planRequest"))
    }
    futurePlan flatMap identity
  }

  def userSignup: Future[User] = {
    val futureUserReference = userService.newUser(userClient.send[UserSignupDetails])
    futureUserReference.map(userBriefing => userBriefing.user)
  }


  trait UserClient {
    def send[T]: T
  }

}



