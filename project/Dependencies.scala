import sbt._

object Dependencies {

  val catsEffect = "org.typelevel" %% "cats-effect" % "2.0.0"
  val datastaxJavaDriverCore = "com.datastax.oss" % "java-driver-core" % Versions.datastaxJavaDriverCore
  val doobie = "org.tpolecat" %% "doobie-core" % Versions.doobie
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Versions.doobie
  val flywayCore = "org.flywaydb" % "flyway-core" % "6.1.1"
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % Versions.http4s
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
  val http4sClient = "org.http4s" %% "http4s-client" % Versions.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Versions.http4s
  val http4sServer = "org.http4s" %% "http4s-server" % Versions.http4s
  val jsr305 = "com.google.code.findbugs" % "jsr305" % "3.0.2"
  val kindProjector = "org.typelevel" %% "kind-projector" % "0.10.3"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val micrometerCore = "io.micrometer" % "micrometer-core" % Versions.micrometerCore
  val micrometerJmx = "io.micrometer" % "micrometer-registry-jmx" % Versions.micrometerJmx
  val micrometerStatsD = "io.micrometer" % "micrometer-registry-statsd" % Versions.micrometerStatsD
  val monixEval = "io.monix" %% "monix-eval" % Versions.monix
  val monixCatnap = "io.monix" %% "monix-catnap" % Versions.monix
  val postgresql = "org.postgresql" % "postgresql" % "42.2.9"
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.12.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.1.0"
  val scalazzi = "com.github.vovapolu" %% "scaluzzi" % "0.1.3"
  val silencer = "com.github.ghik" % "silencer-plugin" % Versions.silencer cross CrossVersion.full
  val silencerLib = "com.github.ghik" % "silencer-lib" % Versions.silencer cross CrossVersion.full
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.29"
  val sslConfig = "com.typesafe" %% "ssl-config-core" % "0.4.1"
  val zio = "dev.zio" %% "zio" % "1.0.0-RC17"
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC10"

  object Versions {

    val datastaxJavaDriverCore = "4.3.1"
    val doobie = "0.7.1"
    val http4s = "0.20.15"
    val micrometerCore = "1.3.2"
    val micrometerJmx = "1.3.2"
    val micrometerStatsD = "1.3.2"
    val monix = "3.1.0"
    val silencer = "1.4.4"

  }

}
