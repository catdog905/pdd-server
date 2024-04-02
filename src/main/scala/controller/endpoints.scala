package ru.catdog905
package controller

import domain._
import domain.errors.AppError
//import domain.errors._
import sttp.tapir._
import sttp.tapir.json.circe._

object endpoints {
  val rewardUser
  : PublicEndpoint[(RequestContext, Replenishment), AppError, Unit, Any] =
    endpoint.post
      .in("user_replenishment")
      .in(header[RequestContext]("X-Request-Id"))
      .in(jsonBody[Replenishment])
      .errorOut(jsonBody[AppError])
}
