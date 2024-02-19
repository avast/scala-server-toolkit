import sbt._

object Dependencies {

  val catsEffect = "org.typelevel" %% "cats-effect" % "2.5.5"
  val datastaxJavaDriverCore = "com.datastax.oss" % "java-driver-core" % Versions.datastaxJavaDriverCore
  val doobie = "org.tpolecat" %% "doobie-core" % Versions.doobie
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Versions.doobie
  val flywayCore = "org.flywaydb" % "flyway-core" % "9.12.0"
  val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % "1.10.0"
  val grpcNettyShaded = "io.grpc" % "grpc-netty-shaded" % Versions.grpc
  val grpcProtobuf = "io.grpc" % "grpc-protobuf" % Versions.grpc
  val grpcStub = "io.grpc" % "grpc-stub" % Versions.grpc
  val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % Versions.http4s
  val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.http4s
  val http4sEmberClient = "org.http4s" %% "http4s-ember-client" % Versions.http4s
  val http4sEmberServer = "org.http4s" %% "http4s-ember-server" % Versions.http4s
  val http4sClient = "org.http4s" %% "http4s-client" % Versions.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Versions.http4s
  val http4sServer = "org.http4s" %% "http4s-server" % Versions.http4s
  val jacksonDatabind = "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.3"
  val jetbrainsAnnotations = "org.jetbrains" % "annotations" % "24.0.0"
  val jsr305 = "com.google.code.findbugs" % "jsr305" % "3.0.2"
  val kindProjector = "org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full
  val lettuce = "io.lettuce" % "lettuce-core" % "6.2.2.RELEASE"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.4.14"
  val micrometerCore = "io.micrometer" % "micrometer-core" % Versions.micrometerCore
  val micrometerJmx = "io.micrometer" % "micrometer-registry-jmx" % Versions.micrometerJmx
  val micrometerStatsD = "io.micrometer" % "micrometer-registry-statsd" % Versions.micrometerStatsD
  val micrometerPrometheus = "io.micrometer" % "micrometer-registry-prometheus" % Versions.micrometerPrometheus
  val monixCatnap = "io.monix" %% "monix-catnap" % Versions.monix
  val monixEval = "io.monix" %% "monix-eval" % Versions.monix
  val postgresql = "org.postgresql" % "postgresql" % "42.5.1"
  val pureConfigCore = "com.github.pureconfig" %% "pureconfig-core" % Versions.pureConfig
  val pureConfigGeneric = "com.github.pureconfig" %% "pureconfig-generic" % Versions.pureConfig
  val scalaCollectionCompat = "org.scala-lang.modules" %% "scala-collection-compat" % "2.9.0"
  val scalafixScaluzzi = "com.github.vovapolu" %% "scaluzzi" % "0.1.21"
  val scalafixOrganizeImports = "com.github.liancheng" %% "organize-imports" % "0.6.0"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.15"
  val sentry = "io.sentry" % "sentry" % "6.13.0"
  val slf4jApi = "org.slf4j" % "slf4j-api" % "2.0.6"
  val sslConfig = "com.typesafe" %% "ssl-config-core" % "0.6.1"
  val testContainersScalaScalaTest = "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.12"
  val testContainersScalaKafka = "com.dimafeng" %% "testcontainers-scala-kafka" % "0.40.12"
  val zio = "dev.zio" %% "zio" % "1.0.17"
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % "2.5.1.0"

  object Versions {

    val datastaxJavaDriverCore = "4.17.0"
    val doobie = "0.13.4"
    val grpc = "1.52.1"
    val http4s = "0.22.14"
    val micrometerCore = "1.10.3"
    val micrometerJmx = "1.10.3"
    val micrometerStatsD = "1.10.3"
    val micrometerPrometheus = "1.10.3"
    val monix = "3.4.1"
    val pureConfig = "0.17.1"

  }

}
