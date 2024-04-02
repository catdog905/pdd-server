package ru.catdog905
package controller

import domain.RequestContext
import storage.UserStorage

import cats.data.ReaderT
import cats.effect.IO
import sttp.tapir.server.ServerEndpoint

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