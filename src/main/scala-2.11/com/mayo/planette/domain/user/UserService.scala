package com.mayo.planette.domain.user

import com.mayo.planette.domain.communication.Conversation

import scala.concurrent.Future

/**
 * @author yoav @since 5/6/16.
 */
trait UserService {

  type UserSignupDetails
  type User <: UserMandatoryProperties
  type UserResources
  type UserFamily <: Family
  type UserReference
  type FamilyBriefing <: Conversation with FamilyBriefingMandatoryProperties

  def newUser(user: UserSignupDetails): Future[FamilyBriefing]

  def getUser(userId: User#Id): Future[User]

  def login(user: User): Future[Unit]

  def introduceFamily(userFamily: UserFamily): Future[Unit]

  def introduceFamilyMember(familyMember: UserFamily#FamilyMember): Future[Unit]

  def declareResource(userResources: UserResources): Future[Unit]

  trait Family{
    type FamilyMember
    type Offspring <: FamilyMember
    type Sibling <: FamilyMember
    type Wife <: FamilyMember
    type Father <: FamilyMember
    type Mother <: FamilyMember
    type Son <: Offspring
    type Daughter <: Offspring
    type Brother <: Sibling
    type Sister <: Sibling
    type Husband <: FamilyMember
    val wife: Option[Wife] = None
    val husband: Option[Husband] = None
    val offsprings: List[Offspring] = Nil
    val siblings: List[Sibling] = Nil
    val father: Option[Father] = None
    val mother: Option[Mother] = None
  }

  trait UserMandatoryProperties{
    type Id
    val id: Id
    val family: Option[UserFamily]
  }

  trait FamilyBriefingMandatoryProperties {
    val user: User
  }
}

