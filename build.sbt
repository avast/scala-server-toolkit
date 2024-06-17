import sbt.Keys.scalacOptions

import scala.collection.Seq

def pureconfig = libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) =>
      List(
        Dependencies.pureConfigCore
      )
    case Some((2, _)) =>
      List(
        Dependencies.pureConfigCore,
        Dependencies.pureConfigGeneric
      )
    case _ => List.empty
  }
}

lazy val root = project
  .in(file("."))
  .aggregate(
    appMonix,
    appZio,
    bundleMonixHttp4sBlaze,
    bundleMonixHttp4sEmber,
    bundleZioHttp4sBlaze,
    bundleZioHttp4sEmber,
    cassandraDatastaxDriver,
    cassandraDatastaxDriverPureConfig,
    catsEffect,
    doobieHikari,
    doobieHikariPureConfig,
    example,
    flyway,
    flywayPureConfig,
    fs2Kafka,
    fs2KafkaPureConfig,
    grpcServer,
    grpcServerMicrometer,
    grpcServerPureConfig,
    http4sClientBlaze,
    http4sClientBlazePureConfig,
    http4sClientEmber,
    http4sClientEmberPureConfig,
    http4sClientMonixCatnap,
    http4sServer,
    http4sServerBlaze,
    http4sServerBlazePureConfig,
    http4sServerEmber,
    http4sServerEmberPureConfig,
    http4sServerMicrometer,
    jdkHttpClient,
    jdkHttpClientPureConfig,
    jvm,
    jvmMicrometer,
    jvmPureConfig,
    lettuce,
    lettucePureConfig,
    micrometer,
    micrometerJmx,
    micrometerJmxPureConfig,
    micrometerPrometheus,
    micrometerPrometheusPureConfig,
    micrometerStatsD,
    micrometerStatsDPureConfig,
    monixCatnap,
    monixCatnapMicrometer,
    monixCatnapPureConfig,
    pureConfig,
    sentry,
    sentryPureConfig,
    sslConfig
  )
  .disablePlugins(SbtVersionPolicyPlugin)
  .settings(BuildSettings.common)
  .settings(
    name := "scala-server-toolkit",
    publish / skip := true
  )

lazy val appMonix = project
  .in(file("app-monix"))
  .dependsOn(
    http4sServerMicrometer,
    jvmMicrometer,
    jvmPureConfig,
    pureConfig
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-app-monix",
    libraryDependencies += Dependencies.monixEval
  )

lazy val bundleMonixHttp4sBlaze = project
  .in(file("bundle-monix-http4s-blaze"))
  .dependsOn(
    http4sClientBlaze,
    http4sClientBlazePureConfig,
    http4sServerBlaze,
    http4sServerBlazePureConfig,
    appMonix
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-bundle-monix-http4s-blaze"
  )

lazy val bundleMonixHttp4sEmber = project
  .in(file("bundle-monix-http4s-ember"))
  .dependsOn(
    http4sClientEmber,
    http4sClientEmberPureConfig,
    http4sServerEmber,
    http4sServerEmberPureConfig,
    appMonix
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-bundle-monix-http4s-ember"
  )

lazy val appZio = project
  .in(file("app-zio"))
  .dependsOn(
    http4sServerMicrometer,
    jvmMicrometer,
    jvmPureConfig,
    pureConfig
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-app-zio",
    libraryDependencies ++= Seq(
      Dependencies.zio,
      Dependencies.zioInteropCats
    )
  )

lazy val bundleZioHttp4sBlaze = project
  .in(file("bundle-zio-http4s-blaze"))
  .dependsOn(
    http4sClientBlaze,
    http4sClientBlazePureConfig,
    http4sServerBlaze,
    http4sServerBlazePureConfig,
    appZio
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-bundle-zio-http4s-blaze"
  )

lazy val bundleZioHttp4sEmber = project
  .in(file("bundle-zio-http4s-ember"))
  .dependsOn(
    http4sClientEmber,
    http4sClientEmberPureConfig,
    http4sServerEmber,
    http4sServerEmberPureConfig,
    appZio
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-bundle-zio-http4s-ember"
  )

lazy val cassandraDatastaxDriver = project
  .in(file("cassandra-datastax-driver"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-cassandra-datastax-driver",
    libraryDependencies += Dependencies.datastaxJavaDriverCore
  )

lazy val cassandraDatastaxDriverPureConfig = project
  .in(file("cassandra-datastax-driver-pureconfig"))
  .dependsOn(cassandraDatastaxDriver)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-cassandra-datastax-driver-pureconfig",
    pureconfig
  )

lazy val catsEffect = project
  .in(file("cats-effect"))
  .settings(BuildSettings.common)
  .settings(name := "sst-cats-effect")

lazy val doobieHikari = project
  .in(file("doobie-hikari"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-doobie-hikari",
    libraryDependencies ++= Seq(
      Dependencies.doobie,
      Dependencies.doobieHikari
    )
  )

lazy val doobieHikariPureConfig = project
  .in(file("doobie-hikari-pureconfig"))
  .dependsOn(doobieHikari)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-doobie-hikari-pureconfig",
    pureconfig
  )

lazy val example = project
  .in(file("example"))
  .disablePlugins(SbtVersionPolicyPlugin)
  .dependsOn(
    bundleZioHttp4sBlaze,
    cassandraDatastaxDriver,
    cassandraDatastaxDriverPureConfig,
    doobieHikari,
    doobieHikariPureConfig,
    flyway,
    flywayPureConfig,
    http4sClientBlazePureConfig,
    http4sClientMonixCatnap,
    monixCatnapPureConfig,
    micrometerJmxPureConfig,
    sslConfig
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-example",
    publish / skip := true,
    libraryDependencies ++= Seq(
      Dependencies.logbackClassic,
      Dependencies.postgresql
    )
  )

lazy val flyway = project
  .in(file("flyway"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-flyway",
    libraryDependencies ++= List(
      Dependencies.scalaCollectionCompat,
      Dependencies.flywayCore
    )
  )

lazy val flywayPureConfig = project
  .in(file("flyway-pureconfig"))
  .dependsOn(flyway)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-flyway-pureconfig",
    pureconfig
  )

lazy val fs2Kafka = project
  .in(file("fs2-kafka"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-fs2-kafka",
    libraryDependencies ++= Seq(
      Dependencies.fs2Kafka,
      Dependencies.testContainersScalaScalaTest % Test,
      Dependencies.testContainersScalaKafka % Test,
      Dependencies.jacksonDatabind % Test
    )
  )

lazy val fs2KafkaPureConfig = project
  .in(file("fs2-kafka-pureconfig"))
  .dependsOn(fs2Kafka)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-fs2-kafka-pureconfig",
    pureconfig
  )

lazy val grpcServer = project
  .in(file("grpc-server"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-grpc-server",
    libraryDependencies ++= Seq(
      Dependencies.grpcNettyShaded,
      Dependencies.grpcProtobuf,
      Dependencies.grpcStub,
      Dependencies.slf4jApi
    )
  )

lazy val grpcServerMicrometer = project
  .in(file("grpc-server-micrometer"))
  .dependsOn(grpcServer)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-grpc-server-micrometer",
    libraryDependencies += Dependencies.micrometerCore
  )

lazy val grpcServerPureConfig = project
  .in(file("grpc-server-pureconfig"))
  .dependsOn(grpcServer)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-grpc-server-pureconfig",
    pureconfig
  )

lazy val http4sClientBlaze = project
  .in(file("http4s-client-blaze"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-client-blaze",
    libraryDependencies += Dependencies.http4sBlazeClient
  )

lazy val http4sClientEmber = project
  .in(file("http4s-client-ember"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-client-ember",
    libraryDependencies += Dependencies.http4sEmberClient
  )

lazy val http4sClientBlazePureConfig = project
  .in(file("http4s-client-blaze-pureconfig"))
  .dependsOn(http4sClientBlaze, jvmPureConfig)
  .settings(BuildSettings.common)
  .settings(name := "sst-http4s-client-blaze-pureconfig")

lazy val http4sClientEmberPureConfig = project
  .in(file("http4s-client-ember-pureconfig"))
  .dependsOn(http4sClientEmber, jvmPureConfig)
  .settings(BuildSettings.common)
  .settings(name := "sst-http4s-client-ember-pureconfig")

lazy val http4sClientMonixCatnap = project
  .in(file("http4s-client-monix-catnap"))
  .dependsOn(monixCatnapMicrometer)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-client-monix-catnap",
    libraryDependencies += Dependencies.http4sClient
  )

lazy val http4sServer = project
  .in(file("http4s-server"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-server",
    libraryDependencies ++= Seq(
      Dependencies.http4sServer,
      Dependencies.http4sBlazeClient % Test,
      Dependencies.http4sBlazeServer % Test,
      Dependencies.http4sDsl % Test
    )
  )

lazy val http4sServerBlaze = project
  .in(file("http4s-server-blaze"))
  .dependsOn(http4sServer, http4sClientBlaze % Test)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-server-blaze",
    libraryDependencies ++= Seq(
      Dependencies.http4sBlazeServer,
      Dependencies.http4sDsl,
      Dependencies.slf4jApi
    )
  )

lazy val http4sServerEmber = project
  .in(file("http4s-server-ember"))
  .dependsOn(http4sServer, http4sClientEmber % Test)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-server-ember",
    libraryDependencies ++= Seq(
      Dependencies.http4sEmberServer,
      Dependencies.http4sDsl,
      Dependencies.slf4jApi
    )
  )

lazy val http4sServerBlazePureConfig = project
  .in(file("http4s-server-blaze-pureconfig"))
  .dependsOn(http4sServerBlaze)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-server-blaze-pureconfig",
    pureconfig
  )

lazy val http4sServerEmberPureConfig = project
  .in(file("http4s-server-ember-pureconfig"))
  .dependsOn(http4sServerEmber)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-server-ember-pureconfig",
    pureconfig
  )

lazy val http4sServerMicrometer = project
  .in(file("http4s-server-micrometer"))
  .dependsOn(http4sServer)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-server-micrometer",
    libraryDependencies ++= Seq(
      Dependencies.micrometerCore,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

lazy val jdkHttpClient = project
  .in(file("jdk-http-client"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-jdk-http-client"
  )

lazy val jdkHttpClientPureConfig = project
  .in(file("jdk-http-client-pureconfig"))
  .dependsOn(jdkHttpClient)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-jdk-http-client-pureconfig",
    pureconfig
  )

lazy val jvm = project
  .in(file("jvm"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-jvm",
    libraryDependencies += Dependencies.slf4jApi
  )

lazy val jvmMicrometer = project
  .in(file("jvm-micrometer"))
  .dependsOn(jvm)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-jvm-micrometer",
    libraryDependencies ++= Seq(
      Dependencies.micrometerCore,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

lazy val jvmPureConfig = project
  .in(file("jvm-pureconfig"))
  .dependsOn(jvm)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-jvm-pureconfig",
    pureconfig
  )

lazy val lettuce = project
  .in(file("lettuce"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-lettuce",
    libraryDependencies += Dependencies.lettuce
  )

lazy val lettucePureConfig = project
  .in(file("lettuce-pureconfig"))
  .dependsOn(lettuce)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-lettuce-pureconfig",
    pureconfig
  )

lazy val micrometer = project
  .in(file("micrometer"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-micrometer",
    libraryDependencies ++= Seq(
      Dependencies.micrometerCore,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

lazy val micrometerJmx = project
  .in(file("micrometer-jmx"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-micrometer-jmx",
    libraryDependencies ++= Seq(
      Dependencies.micrometerJmx,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

lazy val micrometerJmxPureConfig = project
  .in(file("micrometer-jmx-pureconfig"))
  .dependsOn(micrometerJmx)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-micrometer-jmx-pureconfig",
    pureconfig
  )

lazy val micrometerPrometheus = project
  .in(file("micrometer-prometheus"))
  .dependsOn(micrometer)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-micrometer-prometheus",
    libraryDependencies ++= Seq(
      Dependencies.micrometerPrometheus,
      Dependencies.monixEval % Test,
      Dependencies.monixExecution % Test,
      Dependencies.http4sPrometheus % Test
    )
  )

lazy val micrometerPrometheusPureConfig = project
  .in(file("micrometer-prometheus-pureconfig"))
  .dependsOn(micrometerPrometheus)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-micrometer-prometheus-pureconfig",
    pureconfig
  )

lazy val micrometerStatsD = project
  .in(file("micrometer-statsd"))
  .dependsOn(micrometer)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-micrometer-statsd",
    libraryDependencies ++= Seq(
      Dependencies.micrometerStatsD,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

lazy val micrometerStatsDPureConfig = project
  .in(file("micrometer-statsd-pureconfig"))
  .dependsOn(micrometerStatsD)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-micrometer-statsd-pureconfig",
    pureconfig
  )

lazy val monixCatnap = project
  .in(file("monix-catnap"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-monix-catnap",
    libraryDependencies ++= Seq(
      Dependencies.monixCatnap,
      Dependencies.slf4jApi
    )
  )

lazy val monixCatnapMicrometer = project
  .in(file("monix-catnap-micrometer"))
  .dependsOn(monixCatnap)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-monix-catnap-micrometer",
    libraryDependencies ++= Seq(
      Dependencies.micrometerCore,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

lazy val monixCatnapPureConfig = project
  .in(file("monix-catnap-pureconfig"))
  .dependsOn(monixCatnap)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-monix-catnap-pureconfig",
    pureconfig
  )

lazy val pureConfig = project
  .in(file("pureconfig"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-pureconfig",
    pureconfig
  )

lazy val sentry = project
  .in(file("sentry"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-sentry",
    libraryDependencies += Dependencies.sentry
  )

lazy val sentryPureConfig = project
  .in(file("sentry-pureconfig"))
  .dependsOn(sentry)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-sentry-pureconfig",
    pureconfig
  )

lazy val site = project
  .in(file("site"))
  .enablePlugins(
    MicrositesPlugin,
    MdocPlugin,
    SiteScaladocPlugin,
    ScalaUnidocPlugin
  )
  .dependsOn(
    bundleZioHttp4sBlaze,
    cassandraDatastaxDriver,
    cassandraDatastaxDriverPureConfig,
    doobieHikari,
    doobieHikariPureConfig,
    example,
    flyway,
    flywayPureConfig,
    fs2Kafka,
    http4sClientBlazePureConfig,
    http4sClientMonixCatnap,
    lettucePureConfig,
    monixCatnapPureConfig,
    micrometerJmxPureConfig,
    sentry,
    sslConfig
  )
  .settings(BuildSettings.common)
  .settings(BuildSettings.microsite)
  .settings(
    libraryDependencies += "org.scalameta" %% "mdoc" % "2.5.2" excludeAll (
      ExclusionRule(organization = "org.slf4j"),
      ExclusionRule(organization = "org.scala-lang.modules", name = "scala-collection-compat_2.13")
    ),
    publish / skip := true,
    scalacOptions --= Seq(
      "-Xfatal-warnings",
      "-Xlint:infer-any",
      "-Wvalue-discard",
      "-Wnonunit-statement",
      "-Wunused:implicits",
      "-Wunused:explicits",
      "-Wunused:imports",
      "-Wunused:locals",
      "-Wunused:params",
      "-Wunused:privates"
    )
  )

lazy val sslConfig = project
  .in(file("ssl-config"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-ssl-config",
    libraryDependencies ++= Seq(
      Dependencies.slf4jApi,
      Dependencies.sslConfig
    )
  )

addCommandAlias(
  "check",
  "scalafmtSbtCheck; scalafmtCheckAll; Compile/scalafix --check; Test/scalafix --check; +doc; site/mdoc; +test"
)
addCommandAlias("fix", "Compile/scalafix; Test/scalafix; scalafmtSbt; scalafmtAll")
