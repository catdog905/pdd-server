package ru.catdog905
package storage

import dao.UserSql
import domain.{Replenishment, User, UserName}

import cats.effect.testing.scalatest.AsyncIOSpec
import doobie.implicits._
import io.github.liquibase4s.cats.CatsMigrationHandler.liquibaseHandlerForCats
import org.scalatest.BeforeAndAfter
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

class UserStorageSpec extends AsyncFreeSpec with AsyncIOSpec with BeforeAndAfter with Matchers {

  "PostgresUserStorage" - {
    "should create new User" in {
      PostgresDataBaseFactory.transactor
        .use { transactor =>
          PostgresUserStorage(UserSql.make, transactor).addUser(User(UserName("k.ya")))
            .flatMap { _ =>
              sql"SELECT * FROM pdd.user WHERE gitlab_name = 'k.ya'"
                .query[String]
                .unique
                .transact(transactor)
                .attempt
            }
        }
        .asserting {
          case Left(error) => fail(s"Failed to create a user with such ChatId + $error")
          case Right(name) => name shouldEqual "k.ya"
        }
    }

    "should create new Replenishment" in {
      PostgresDataBaseFactory.transactor
        .use { transactor => {
          val storage = PostgresUserStorage(UserSql.make, transactor)
          for {
            userCreationResult <- storage.addUser(User(UserName("k.ya")))
            replenishmentCreationResult <- storage.addUserReplenishment(Replenishment("0-09786", UserName("k.ya"), 30))
            result <- sql"SELECT ticket_id, user_name, reward FROM pdd.user_replenishment WHERE user_name = 'k.ya'"
              .query[(String, String, Int)]
              .unique
              .transact(transactor)
              .attempt
          } yield (userCreationResult, replenishmentCreationResult, result)
        }
        }
        .asserting {
          case (userCreationResult, replenishmentCreationResult, Left(error)) => {
            println(userCreationResult)
            println(replenishmentCreationResult)
            fail(s"Failed to create a user with such ChatId + $error")
          }
          case (_, _, Right(name)) => name shouldEqual("0-09786", "k.ya", 30)
        }
    }
  }
}
