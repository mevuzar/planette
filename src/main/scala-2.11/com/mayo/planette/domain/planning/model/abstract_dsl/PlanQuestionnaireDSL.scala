package com.mayo.planette.domain.planning.model.abstract_dsl

import com.mayo.whatis.lang.Word

/**
 * @author yoav @since 6/27/16.
 *
 * 1. Every domain has a different completion term
 * 2. "Completion term" is either a final "product" or "state" every plan has
 * 3. Each plan completion is preceded by a final action
 */


trait PlanQuestionnaireDSL {

  type CompletionOutputTerm <: CompletionOutputType
  type CompletionTerm <: Word
  type MaterielTerm <: Word
  type Action <: ActionMandatoryProperties

  trait ActionMandatoryProperties{
    type ActionType
    type ActionsSubject
  }

  val completionOutputTerm: CompletionOutputTerm
  val completionTerm: CompletionTerm

  trait CompletionOutputType
  trait State extends CompletionOutputType
  trait Product extends CompletionOutputType
}

trait Ready extends Word
case object Ready extends Ready


trait CookingQuestionnaireDSL extends PlanQuestionnaireDSL {

  override type CompletionOutputTerm = Dish with Product
  override type CompletionTerm = Ready
  override type MaterielTerm <: Ingrediants
  override type Action = CookingAction
  type CookingAction <: ActionMandatoryProperties

  trait Ingrediants extends Word
  case object Ingrediants extends Ingrediants
  trait Dish extends Word
  case object Dish extends Dish
}

