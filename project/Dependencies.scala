import sbt._

object Dependencies {

  val catsEffect = "org.typelevel" %% "cats-effect" % "2.0.0"
  val kindProjector = "org.typelevel" %% "kind-projector" % "0.10.3"
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.12.1"
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.28"
  val zio = "dev.zio" %% "zio" % "1.0.0-RC13"
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC4"

  object Test {

    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8" % sbt.Test

  }

}
