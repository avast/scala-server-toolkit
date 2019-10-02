import sbt._

object Dependencies {

  val akka = "com.typesafe.akka" %% "akka-stream" % "2.5.21"
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.1.7"
  val catsEffect = "org.typelevel" %% "cats-effect" % "2.0.0"
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % Versions.http4s
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Versions.http4s
  val kindProjector = "org.typelevel" %% "kind-projector" % "0.10.3"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.12.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.8.0-beta4"
  val slf4jSimple = "org.slf4j" % "slf4j-simple" % "1.8.0-beta4"
  val zio = "dev.zio" %% "zio" % "1.0.0-RC13"
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC4"

  object Versions {

    val http4s = "0.20.11"

  }

}
