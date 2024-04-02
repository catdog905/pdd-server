ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "pdd-server",
    idePackagePrefix := Some("ru.catdog905"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.5.0",
      "tf.tofu" %% "derevo-circe" % "0.13.0",
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC2",
      "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC2",
      "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC2",
      "com.github.pureconfig" %% "pureconfig" % "0.17.4",
      "io.github.liquibase4s" %% "liquibase4s-core" % "1.0.0",
      "io.github.liquibase4s" %% "liquibase4s-cats-effect" % "1.0.0",
      "com.github.nscala-time" %% "nscala-time" % "2.32.0",
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.9.8",
      "com.softwaremill.sttp.tapir" %% "tapir-cats-effect" % "1.10.0",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.9.8",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.9.8",
      "com.softwaremill.sttp.tapir" %% "tapir-derevo" % "1.9.8",
      "org.http4s" %% "http4s-ember-server" % "0.23.19",
      "org.http4s" %% "http4s-circe" % "0.23.19",
      "org.testcontainers" % "testcontainers" % "1.19.4" % Test,
      "org.testcontainers" % "postgresql" % "1.19.3" % Test,
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
      "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0" % Test,
    ),
    dependencyOverrides += "io.circe" %% "circe-core" % "0.14.6",
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.3" cross CrossVersion.full),
    scalacOptions ++= Seq("-Ymacro-annotations")
  )
