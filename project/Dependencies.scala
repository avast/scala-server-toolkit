import sbt._

object Dependencies {

  object Versions {
    val catsEffect = "2.0.0"
    val kindProjector = "0.10.3"
    val pureConfig = "0.12.0"
    val scalaTest = "3.0.8"
    val zio = "1.0.0-RC13"
    val zioInteropCats = "2.0.0.0-RC4"
    val silencer = "1.4.3"
    val scalazzi = "0.1.3"
    val betterMonadicFor = "0.3.1"
    val slf4jApi = "1.7.28"
  }

  val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
  val kindProjector = "org.typelevel" %% "kind-projector" % Versions.kindProjector
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.pureConfig
  val slf4jApi = "org.slf4j" % "slf4j-api" % Versions.slf4jApi
  val zio = "dev.zio" %% "zio" % Versions.zio
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % Versions.zioInteropCats
  val silencer = "com.github.ghik" % "silencer-plugin" % Versions.silencer cross CrossVersion.full
  val silencerLib = "com.github.ghik" % "silencer-lib" % Versions.silencer cross CrossVersion.full
  val scalazzi = "com.github.vovapolu" %% "scaluzzi" % Versions.scalazzi
  val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor

  object Test {

    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest

  }

}
