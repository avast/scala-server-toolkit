lazy val root = project
  .in(file("."))
  .aggregate(
    bundleMonixHttp4sBlaze,
    bundleZioHttp4sBlaze,
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
    http4sClientJdk,
    http4sClientMonixCatnap,
    http4sServer,
    http4sServerBlaze,
    http4sServerBlazePureConfig,
    http4sServerMicrometer,
    jvm,
    jvmMicrometer,
    jvmPureConfig,
    lettuce,
    lettucePureConfig,
    micrometer,
    micrometerJmx,
    micrometerJmxPureConfig,
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
  .settings(BuildSettings.common)
  .settings(
    name := "scala-server-toolkit",
    publish / skip := true
  )

lazy val bundleMonixHttp4sBlaze = project
  .in(file("bundle-monix-http4s-blaze"))
  .dependsOn(
    http4sClientBlaze,
    http4sClientBlazePureConfig,
    http4sServerBlaze,
    http4sServerBlazePureConfig,
    http4sServerMicrometer,
    jvmMicrometer,
    jvmPureConfig,
    pureConfig
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-bundle-monix-http4s-blaze",
    libraryDependencies += Dependencies.monixEval
  )

lazy val bundleZioHttp4sBlaze = project
  .in(file("bundle-zio-http4s-blaze"))
  .dependsOn(
    http4sClientBlaze,
    http4sClientBlazePureConfig,
    http4sServerBlaze,
    http4sServerBlazePureConfig,
    http4sServerMicrometer,
    jvmMicrometer,
    jvmPureConfig,
    pureConfig
  )
  .settings(BuildSettings.common)
  .settings(
    name := "sst-bundle-zio-http4s-blaze",
    libraryDependencies ++= Seq(
      Dependencies.zio,
      Dependencies.zioInteropCats
    )
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
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
  )

lazy val example = project
  .in(file("example"))
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
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
  )

lazy val http4sClientBlaze = project
  .in(file("http4s-client-blaze"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-client-blaze",
    libraryDependencies += Dependencies.http4sBlazeClient
  )

lazy val http4sClientBlazePureConfig = project
  .in(file("http4s-client-blaze-pureconfig"))
  .dependsOn(http4sClientBlaze, jvmPureConfig)
  .settings(BuildSettings.common)
  .settings(name := "sst-http4s-client-blaze-pureconfig")

lazy val http4sClientJdk = project
  .in(file("http4s-client-jdk"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-client-jdk",
    libraryDependencies += Dependencies.http4sJdkHttpClient
  )

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

lazy val http4sServerBlazePureConfig = project
  .in(file("http4s-server-blaze-pureconfig"))
  .dependsOn(http4sServerBlaze)
  .settings(BuildSettings.common)
  .settings(
    name := "sst-http4s-server-blaze-pureconfig",
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
  )

lazy val pureConfig = project
  .in(file("pureconfig"))
  .settings(BuildSettings.common)
  .settings(
    name := "sst-pureconfig",
    libraryDependencies += Dependencies.pureConfig
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
    libraryDependencies += Dependencies.pureConfig
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
    publish / skip := true,
    scalacOptions := scalacOptions.value.filterNot(_ == "-Xfatal-warnings").filterNot(_ == "-Xlint:infer-any")
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
  "; scalafmtSbtCheck; scalafmtCheckAll; +test"
)
addCommandAlias("fix", "; compile:scalafix; test:scalafix; scalafmtSbt; scalafmtAll")
