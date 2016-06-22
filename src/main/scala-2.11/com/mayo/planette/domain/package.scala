package com.mayo.planette

import scala.concurrent.Future
import scala.xml.dtd.ContentModel._labelT

/**
 * @author yoav @since 6/21/16.
 */
package object domain {
  type ServerOperation[Request, Response] = Request => Future[Response]

  trait WithId[Id]{
    val id: Id
  }

}
