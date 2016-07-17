//package com.mayo.planette.abstraction.terminology
//package common
//
//import com.mayo.planette.abstraction.terminology.ServiceDSL.ServiceOperations.serviceOperation
//
//import scalaz.{Free, Id, ~>}
//import ServiceDSL._
//
///**
// * @author yoav @since 7/12/16.
// */
//object ServiceCommunicationF {
//
////  object DSL {
////    object ServiceOperation {
////      def serviceOperation[A](service: ServiceMethodCall[A]): Free[Servable, A] =
////        Free.liftFC(ServerCall(service): ServiceOperation[A])
////    }
////
////  }
//
//}
//
//object Example extends App {
//
//  import java.util.UUID
//
//  case class SignUpDetails(name: String, password: String)
//
//  case class SignupResponse(token: UUID, refreshToken: UUID)
//
//  case class UserToken(token: UUID)
//
//  sealed trait AccountsMethodCall[+A] extends ServiceMethodCall[A]
//
//  case class SignUpCall(signUpDetails: SignUpDetails) extends AccountsMethodCall[SignupResponse]
//
//  case class SignInCall(token: UUID) extends AccountsMethodCall[UserToken]
//
//  object DirectCallInterpreter extends (ServiceOperation ~> Id.Id) {
//    override def apply[A](fa: ServiceOperation[A]): Id.Id[A] = {
//      fa match {
//        case ServerCall(call) => call match {
//          case SignUpCall(details) => SignupResponse(UUID.randomUUID, UUID.randomUUID)
//          case SignInCall(token) => UserToken(UUID.randomUUID)
//        }
//      }
//    }
//  }
//
//
//  val script = for {
//    s1 <- serviceOperation(SignUpCall(SignUpDetails("yoav", "12345")))
//    s2 <- serviceOperation(SignInCall(s1.token))
//  } yield (s1, s2)
//
//  val scriptResult = Free.runFC(script)(DirectCallInterpreter)
//
//  println(scriptResult)
//}
//
