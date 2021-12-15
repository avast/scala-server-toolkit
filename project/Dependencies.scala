import sbt._

object Dependencies {

  val catsEffect = "org.typelevel" %% "cats-effect" % "2.5.4"
  val datastaxJavaDriverCore = "com.datastax.oss" % "java-driver-core" % Versions.datastaxJavaDriverCore
  val doobie = "org.tpolecat" %% "doobie-core" % Versions.doobie
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Versions.doobie
  val flywayCore = "org.flywaydb" % "flyway-core" % "8.2.2"
  val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % "1.8.0"
  val grpcNettyShaded = "io.grpc" % "grpc-netty-shaded" % Versions.grpc
  val grpcProtobuf = "io.grpc" % "grpc-protobuf" % Versions.grpc
  val grpcStub = "io.grpc" % "grpc-stub" % Versions.grpc
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % Versions.http4s
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
  val http4sClient = "org.http4s" %% "http4s-client" % Versions.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Versions.http4s
  val http4sServer = "org.http4s" %% "http4s-server" % Versions.http4s
  val jacksonDatabind = "com.fasterxml.jackson.core" % "jackson-databind" % "2.13.0"
  val jsr305 = "com.google.code.findbugs" % "jsr305" % "3.0.2"
  val kindProjector = "org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full
  val lettuce = "io.lettuce" % "lettuce-core" % "6.1.5.RELEASE"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.8"
  val micrometerCore = "io.micrometer" % "micrometer-core" % Versions.micrometerCore
  val micrometerJmx = "io.micrometer" % "micrometer-registry-jmx" % Versions.micrometerJmx
  val micrometerStatsD = "io.micrometer" % "micrometer-registry-statsd" % Versions.micrometerStatsD
  val micrometerPrometheus = "io.micrometer" % "micrometer-registry-prometheus" % Versions.micrometerPrometheus
  val monixCatnap = "io.monix" %% "monix-catnap" % Versions.monix
  val monixEval = "io.monix" %% "monix-eval" % Versions.monix
  val postgresql = "org.postgresql" % "postgresql" % "42.3.1"
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.17.1"
  val scalaCollectionCompat = "org.scala-lang.modules" %% "scala-collection-compat" % "2.6.0"
  val scalafixScaluzzi = "com.github.vovapolu" %% "scaluzzi" % "0.1.20"
  val scalafixOrganizeImports = "com.github.liancheng" %% "organize-imports" % "0.6.0"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.10"
  val sentry = "io.sentry" % "sentry" % "5.5.0"
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.32"
  val sslConfig = "com.typesafe" %% "ssl-config-core" % "0.6.0"
  val testContainersScalaScalaTest = "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.39.12"
  val testContainersScalaKafka = "com.dimafeng" %% "testcontainers-scala-kafka" % "0.39.12"
  val zio = "dev.zio" %% "zio" % "1.0.13"
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % "2.5.1.0"

  object Versions {

    val datastaxJavaDriverCore = "4.13.0"
    val doobie = "0.13.4"
    val grpc = "1.42.1"
    val http4s = "0.22.8"
    val micrometerCore = "1.8.1"
    val micrometerJmx = "1.8.1"
    val micrometerStatsD = "1.8.1"
    val micrometerPrometheus = "1.8.1"
    val monix = "3.4.0"

  }

}
