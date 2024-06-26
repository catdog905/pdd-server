package ru.catdog905

import config.AppConfig
import controller.UserController
import dao.UserSql
import domain.RequestContext.ContextualIO
import storage.PostgresUserStorage

import cats.effect.kernel.Resource
import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{IpLiteralSyntax, Ipv4Address, Port}
import doobie.Transactor
import io.github.liquibase4s.Liquibase
import io.github.liquibase4s.cats.CatsMigrationHandler.liquibaseHandlerForCats
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import sttp.tapir.server.http4s.Http4sServerInterpreter

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    (for {
      config <- Resource.eval(AppConfig.load)
      transactor = Transactor.fromDriverManager[ContextualIO](
        config.db.driver,
        config.db.url,
        config.db.user,
        config.db.password
      )
      _ <- Liquibase[IO](config.liquibase).migrate().toResource
      _ <- Resource.eval(IO(println("Some text")))
      storage = PostgresUserStorage[ContextualIO](UserSql.make, transactor)
      _ <- Resource.eval(IO(println(config.liquibase)))

      controller: UserController[IO] = UserController.make(storage)
      routes = Http4sServerInterpreter[IO]().toRoutes(controller.all)
      httpApp = Router("/" -> routes).orNotFound
      _ <- EmberServerBuilder
        .default[IO]
        .withHost(
          Ipv4Address.fromString(config.server.host).getOrElse(ipv4"0.0.0.0")
        )
        .withPort(Port.fromInt(config.server.port).getOrElse(port"80"))
        .withHttpApp(httpApp)
        .build
    } yield ()).useForever.as(ExitCode.Success)
}