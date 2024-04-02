package ru.catdog905
package controller

import domain.RequestContext.ContextualIO
import domain.{Replenishment, User, UserName, errors}
import storage.UserStorage

import cats.data.ReaderT
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxEitherId
import io.circe.syntax.EncoderOps
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.server.Router
import org.http4s.{Header, Method, Request, Status}
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.typelevel.ci.CIString
import sttp.tapir.server.http4s.Http4sServerInterpreter

class EndpointsSpec extends AsyncFlatSpec with Matchers {
  it should "work" in {
    // Mock the storage object
    var check = false
    val mockStorage = new UserStorage[ContextualIO] {
      override def addUser(user: User): ContextualIO[Either[errors.AppError, Unit]] =
        ReaderT.liftF(IO {
          ().asRight
        })

      override def addUserReplenishment(replenishment: Replenishment): ContextualIO[Either[errors.AppError, Unit]] = {
        check = true
        ReaderT.liftF(IO {
          ().asRight
        })
      }
    }

    // Create UserController with the mock storage
    val userController = UserController.make(mockStorage)

    // Create HttpRoutes
    val routes = Http4sServerInterpreter[IO]().toRoutes(userController.all)

    // Create HttpApp with routes
    val httpApp = Router("/" -> routes).orNotFound

    // Define a sample user data to send in the request body
    val user = Replenishment("0-09786", UserName("k.ya"), 30)


    // Test HttpApp with a request
    val request = Request[IO](
      Method.POST,
      uri"/user_replenishment")
      .withHeaders(Header.Raw(CIString("X-Request-Id"), "42"))
      .withEntity(user.asJson)
    val response = httpApp.run(request).unsafeRunSync()
    print(response.as[String].unsafeRunSync())

    // Assert response status and body
    response.status shouldBe Status.Ok
    response.as[String].unsafeRunSync() shouldBe ""
    check shouldEqual true
  }
}