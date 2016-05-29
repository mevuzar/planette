package com.mayo.planette.domain.reputation

import com.mayo.planette.domain.communication.NotificationService

import scala.concurrent.Future

/**
 * @author yoav @since 5/8/16.
 */
trait ReputationService {
  type Questionnaire <: QuestionnaireMandatoryProperties
  type Subject
  type Avis

  def processQuestionnaires(questionnaires: Seq[Questionnaire]): Future[Avis]

  trait QuestionnaireMandatoryProperties{
    type Question
    val subject: Subject
    val questions: Seq[Question]
  }

}
