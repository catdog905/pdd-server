package ru.catdog905
package controller

import cats.Functor
import cats.data.ReaderT
import cats.effect.IO
import sttp.tapir.server.ServerEndpoint
import cats.implicits.toFunctorOps
import ru.catdog905.domain.RequestContext
import ru.catdog905.domain.errors.AppError
import ru.catdog905.storage.UserStorage

trait UserController[F[_]] {
  def rewardUser: ServerEndpoint[Any, F]

  def all: List[ServerEndpoint[Any, F]]
}

object UserController {
  final private class Impl(storage: UserStorage[ReaderT[IO, RequestContext, *]]) extends UserController[IO] {

    override val rewardUser: ServerEndpoint[Any, IO] =
      endpoints.rewardUser.serverLogic { case (ctx, replenishment) =>
        storage.addUserReplenishment(replenishment).run(ctx)
      }

    override val all: List[ServerEndpoint[Any, IO]] =
      List(rewardUser)
  }

  def make(storage: UserStorage[ReaderT[IO, RequestContext, *]]): UserController[IO] = new Impl(storage)
}