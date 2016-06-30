package com.mayo.planette.product.scenario

import com.mayo.planette.Applicable
import com.mayo.planette.domain.F
import com.mayo.planette.product.ScriptMocker

import scala.util.Success

/**
 * Created by Owner on 6/30/2016.
 */
trait RecipeScenario extends SignUpWishAndPlanCookingScenario{

  val recipesService: ScriptServices.ScriptRecipesService
  val applicable: Applicable[F]

  def createPlanAndSaveRecipe = {
    val tryPlan = signUpWishAndPlanCookingScenario
    tryPlan match{
      case Success((token, plan)) =>
        type Plan = plan.type

        recipesService.createRecipe(token)(recipesService.serializationBridgitte.toB(plan.get))

      case _ => applicable(new Exception("failure!!!!!"))
    }
  }


}
