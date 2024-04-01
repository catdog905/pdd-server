package ru.catdog905
package dao

import domain._
import doobie._
import doobie.implicits._

trait UserSql {
  def addNewUser(user: User): ConnectionIO[Int]
  def checkUserExistence(gitlab_name: String): ConnectionIO[Boolean]
  def addReplenishment(replenishment: Replenishment): ConnectionIO[Int]
}

object UserSql {
  object sqls {

    def insertUser(user: User): Update0 =
      sql"""
           INSERT INTO pdd.user (gitlab_name)
           VALUES (${user.name})
         """.update

    def checkUserExistenceQuery(gitlab_name: String): Query0[Int] =
      sql"""
           SELECT EXISTS(SELECT 1 FROM pdd.user WHERE gitlab_name = $gitlab_name)
         """.query[Int]

    def insertReplenishment(replenishment: Replenishment): Update0 =
      sql"""
           INSERT INTO pdd.user_replenishment (ticket_id, user_name, reward)
           VALUES (${replenishment.ticketId}, ${replenishment.userName.gitlab_name}, ${replenishment.reward})
         """.update
  }

  private final class Impl extends UserSql {

    import sqls._

    override def addNewUser(user: User): doobie.ConnectionIO[Int] =
      insertUser(user).run

    override def checkUserExistence(gitlab_name: String): doobie.ConnectionIO[Boolean] =
      checkUserExistenceQuery(gitlab_name).unique.map {
        case 0 => false
        case _ => true
      }

    override def addReplenishment(replenishment: Replenishment): doobie.ConnectionIO[Int] =
      insertReplenishment(replenishment).run
  }

  def make: UserSql = new Impl
}
