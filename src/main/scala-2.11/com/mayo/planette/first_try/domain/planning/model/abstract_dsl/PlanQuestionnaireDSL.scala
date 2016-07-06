package com.mayo.planette.first_try.domain.planning.model.abstract_dsl

import com.mayo.planette.first_try.domain.planning.service.PlanningService
import com.mayo.whatis.lang.Word

/**
 * @author yoav @since 6/27/16.
 *
 *         1. Every domain has a different completion term
 *         2. "Completion term" is either a final "product" or "state" every plan has
 *         3. Each plan completion is preceded by a final action
 */


trait PlanQuestionnaireDSL {

  type CompletionOutputTerm <: CompletionOutputType
  type CompletionTerm <: Word
  type MaterielTerm <: Word
  type Action <: ActionMandatoryProperties

  trait ActionMandatoryProperties {
    type ActionType
    type ActionsSubject
  }

  trait CompletionOutputType

  trait State extends CompletionOutputType

  trait Product extends CompletionOutputType

}

trait Ready extends Word

case object Ready extends Ready


trait CookingQuestionnaireDSL extends PlanQuestionnaireDSL {

  override type CompletionOutputTerm = Dish with Product
  override type CompletionTerm = Ready
  override type MaterielTerm = Ingredients
  override type Action = CookingAction

  trait CookingAction extends ActionMandatoryProperties

  trait Ingredients extends Word

  case object Ingredients extends Ingredients

  trait Dish extends Word

  case object Dish extends Dish

}

  /*
  * There are 3 parts to this session:
  * 1. Recipe info(dish name, recipe source etc) questions
  * 2. Ingredient questions
  * 3. Preparation questions
  *
  * Each of those parts is a questionnaire in itself
  * */


