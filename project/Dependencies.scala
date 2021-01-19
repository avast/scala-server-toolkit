import sbt._

object Dependencies {

  val catsEffect = "org.typelevel" %% "cats-effect" % "2.3.1"
  val datastaxJavaDriverCore = "com.datastax.oss" % "java-driver-core" % Versions.datastaxJavaDriverCore
  val doobie = "org.tpolecat" %% "doobie-core" % Versions.doobie
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Versions.doobie
  val flywayCore = "org.flywaydb" % "flyway-core" % "7.5.0"
  val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % "1.3.1"
  val grpcNettyShaded = "io.grpc" % "grpc-netty-shaded" % Versions.grpc
  val grpcProtobuf = "io.grpc" % "grpc-protobuf" % Versions.grpc
  val grpcStub = "io.grpc" % "grpc-stub" % Versions.grpc
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % Versions.http4s
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
  val http4sClient = "org.http4s" %% "http4s-client" % Versions.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Versions.http4s
  val http4sServer = "org.http4s" %% "http4s-server" % Versions.http4s
  val jacksonDatabind = "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.1"
  val jsr305 = "com.google.code.findbugs" % "jsr305" % "3.0.2"
  val kindProjector = "org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full
  val lettuce = "io.lettuce" % "lettuce-core" % "6.0.2.RELEASE"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val micrometerCore = "io.micrometer" % "micrometer-core" % Versions.micrometerCore
  val micrometerJmx = "io.micrometer" % "micrometer-registry-jmx" % Versions.micrometerJmx
  val micrometerStatsD = "io.micrometer" % "micrometer-registry-statsd" % Versions.micrometerStatsD
  val monixCatnap = "io.monix" %% "monix-catnap" % Versions.monix
  val monixEval = "io.monix" %% "monix-eval" % Versions.monix
  val postgresql = "org.postgresql" % "postgresql" % "42.2.18"
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.14.0"
  val scalaCollectionCompat = "org.scala-lang.modules" %% "scala-collection-compat" % "2.3.2"
  val scalafixScaluzzi = "com.github.vovapolu" %% "scaluzzi" % "0.1.16"
  val scalafixOrganizeImports = "com.github.liancheng" %% "organize-imports" % "0.4.4"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.3"
  val sentry = "io.sentry" % "sentry" % "3.2.0"
  val silencer = "com.github.ghik" % "silencer-plugin" % Versions.silencer cross CrossVersion.full
  val silencerLib = "com.github.ghik" % "silencer-lib" % Versions.silencer % Provided cross CrossVersion.full
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.30"
  val sslConfig = "com.typesafe" %% "ssl-config-core" % "0.4.2"
  val testContainersScalaScalaTest = "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.38.8"
  val testContainersScalaKafka = "com.dimafeng" %% "testcontainers-scala-kafka" % "0.38.8"
  val zio = "dev.zio" %% "zio" % "1.0.3"
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % "2.2.0.1"

  object Versions {

    val datastaxJavaDriverCore = "4.10.0"
    val doobie = "0.10.0"
    val grpc = "1.35.0"
    val http4s = "0.21.15"
    val micrometerCore = "1.6.3"
    val micrometerJmx = "1.6.3"
    val micrometerStatsD = "1.6.3"
    val monix = "3.3.0"
    val silencer = "1.7.1"

  }

}
