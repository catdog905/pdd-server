package ru.catdog905
package config

import cats.effect.IO
import io.github.liquibase4s.LiquibaseConfig
import pureconfig.generic.semiauto._
import pureconfig.{ConfigReader, ConfigSource}

final case class AppConfig(
  db: DbConfig,
  server: ServerConfig,
  liquibase: LiquibaseConfig
)
object AppConfig {
  implicit val reader: ConfigReader[AppConfig] = deriveReader
  implicit val liquibaseConfigReader: ConfigReader[LiquibaseConfig] = deriveReader[LiquibaseConfig]

  def load: IO[AppConfig] =
    IO.delay(ConfigSource.default.loadOrThrow[AppConfig])
}

final case class DbConfig(
  url: String,
  driver: String,
  user: String,
  password: String
)
object DbConfig {
  implicit val reader: ConfigReader[DbConfig] = deriveReader
}

final case class ServerConfig(host: String, port: Int)
object ServerConfig {
  implicit val reader: ConfigReader[ServerConfig] = deriveReader
}