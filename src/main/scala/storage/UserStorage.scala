package ru.catdog905
package storage

import cats.effect.{IO, MonadCancelThrow}
import cats.effect.std.Random
import cats.implicits.{catsSyntaxApplicativeError, catsSyntaxEitherId, toFlatMapOps, toFunctorOps}
import cats.syntax.option._
import dao.UserSql

import doobie.Transactor
import doobie.implicits._
import domain.{Replenishment, User, UserName}
import domain.errors.{AppError, CanNotCreateUser, InternalError, NotSuchUserExists}

import cats.FlatMap

trait UserStorage[F[_]] {
  def addUser(user: User): F[Either[AppError, Unit]]

  def addUserReplenishment(replenishment: Replenishment): F[Either[AppError, Unit]]
}

final case class PostgresUserStorage[F[_]: MonadCancelThrow](userSql: UserSql, transactor: Transactor[F]) extends UserStorage[F] {
  override def addUser(user: User): F[Either[AppError, Unit]] = {
    userSql.addNewUser(user).transact(transactor).attempt.map {
      case Left(th) => InternalError(th).asLeft
      case Right(result) if result == 1 => ().asRight
      case Right(result) if result != 1 => CanNotCreateUser.asLeft
    }
  }

  override def addUserReplenishment(replenishment: Replenishment): F[Either[AppError, Unit]] = {
    for {
      _ <- userSql.checkUserExistence(replenishment.userName.gitlab_name).transact(transactor).attempt.map {
        case Left(th) => InternalError(th).asLeft
        case Right(false) => NotSuchUserExists.asLeft
        case Right(true) => ().asRight
      }
      _ <- userSql.addReplenishment(replenishment).transact(transactor).attempt.map {
        case Left(th) => InternalError(th).asLeft
        case Right(1) => ().asRight
        case Right(_) => CanNotCreateUser.asLeft
      }
    } yield ().asRight
  }
}
