import sbt._

object Dependencies {

  val catsEffect = "org.typelevel" %% "cats-effect" % "2.0.0"
  val doobie = "org.tpolecat" %% "doobie-core" % Versions.doobie
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Versions.doobie
  val flywayCore = "org.flywaydb" % "flyway-core" % "6.0.7"
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % Versions.http4s
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Versions.http4s
  val http4sServer = "org.http4s" %% "http4s-server" % Versions.http4s
  val jsr305 = "com.google.code.findbugs" % "jsr305" % "3.0.2"
  val kindProjector = "org.typelevel" %% "kind-projector" % "0.10.3"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val micrometerCore = "io.micrometer" % "micrometer-core" % Versions.micrometer
  val micrometerJmx = "io.micrometer" % "micrometer-registry-jmx" % Versions.micrometer
  val micrometerStatsD = "io.micrometer" % "micrometer-registry-statsd" % Versions.micrometer
  val monixEval = "io.monix" %% "monix-eval" % "3.0.0"
  val postgresql = "org.postgresql" % "postgresql" % "42.2.8"
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.12.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
  val scalazzi = "com.github.vovapolu" %% "scaluzzi" % "0.1.3"
  val silencer = "com.github.ghik" % "silencer-plugin" % Versions.silencer cross CrossVersion.full
  val silencerLib = "com.github.ghik" % "silencer-lib" % Versions.silencer cross CrossVersion.full
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.28"
  val sslConfig = "com.typesafe" %% "ssl-config-core" % "0.4.0"
  val zio = "dev.zio" %% "zio" % "1.0.0-RC15"
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC6"

  object Versions {

    val doobie = "0.7.1"
    val http4s = "0.20.11"
    val micrometer = "1.3.0"
    val silencer = "1.4.4"

  }

}
