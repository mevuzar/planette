package com.mayo.planette.domain.integration.interpreters.user

import com.mayo.planette.domain.communication.ClientHandlingService
import com.mayo.planette.domain.user.UserService

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author yoav @since 5/6/16.
 */
trait PlanetteUserInterpreter extends UserService with ClientHandlingService {
  implicit val ctxt: ExecutionContext = ???

  def userToUserReference(user: User): UserReference

  def userSignUpFlow(userSignupRequest: Request[UserSignupDetails]): Future[Response[UserReference]] = {
    handleRequest[UserSignupDetails, UserReference](userSignupRequest) { signupDetails =>
      newUser(signupDetails).map(r => wrap[UserReference](userToUserReference(r.user)))
    }
  }


}
