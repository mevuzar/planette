package com.mayo.planette.abstraction.terminology.server

import com.mayo.planette.abstraction.terminology.CommonTerms

import scala.util.Try

/**
 * @author yoav @since 7/19/16.
 */
trait AuthenticationService {
  def authenticate[AuthenticationToken](token: AuthenticationToken): Try[Unit]
}
