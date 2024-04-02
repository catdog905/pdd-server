package ru.catdog905
package controller

import domain._

import ru.catdog905.domain.errors.AppError
//import domain.errors._
import sttp.tapir._
import sttp.tapir.generic.auto._
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
