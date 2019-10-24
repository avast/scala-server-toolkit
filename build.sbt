ThisBuild / organization := "com.avast"
ThisBuild / homepage := Some(url("https://github.com/avast/scala-server-toolkit"))
ThisBuild / description := "Functional programming toolkit for building server applications in Scala."
ThisBuild / licenses := Seq("MIT" -> url("https://raw.githubusercontent.com/avast/scala-server-toolkit/master/LICENSE"))
ThisBuild / developers := List(Developer("jakubjanecek", "Jakub Janecek", "janecek@avast.com", url("https://www.avast.com")))

ThisBuild / turbo := true

lazy val commonSettings = BuildSettings.common ++ Seq(
  libraryDependencies ++= Seq(
    Dependencies.catsEffect,
    Dependencies.logbackClassic % Test,
    Dependencies.scalaTest % Test
  ),
  Test / publishArtifact := false
)

lazy val root = project
  .in(file("."))
  .aggregate(
    bundleMonixHttp4sBlaze,
    bundleZioHttp4sBlaze,
    doobieHikari,
    doobieHikariPureConfig,
    example,
    flyway,
    flywayPureConfig,
    http4sClientBlaze,
    http4sClientBlazePureConfig,
    http4sServer,
    http4sServerBlaze,
    http4sServerBlazePureConfig,
    http4sServerMicrometer,
    jvm,
    jvmMicrometer,
    jvmPureConfig,
    micrometerJmx,
    micrometerJmxPureConfig,
    micrometerStatsD,
    monixCatnap,
    monixCatnapPureConfig,
    pureConfig,
    sslConfig
  )
  .settings(
    name := "scala-server-toolkit",
    publish / skip := true
  )

lazy val bundleMonixHttp4sBlaze = project
  .in(file("bundle-monix-http4s-blaze"))
  .dependsOn(http4sClientBlaze, http4sServerBlaze, http4sServerBlazePureConfig, http4sServerMicrometer, jvmMicrometer, jvmPureConfig)
  .settings(commonSettings)
  .settings(
    name := "sst-bundle-monix-http4s-blaze",
    libraryDependencies += Dependencies.monixEval
  )

lazy val bundleZioHttp4sBlaze = project
  .in(file("bundle-zio-http4s-blaze"))
  .dependsOn(http4sClientBlaze, http4sServerBlaze, http4sServerBlazePureConfig, http4sServerMicrometer, jvmMicrometer, jvmPureConfig)
  .settings(commonSettings)
  .settings(
    name := "sst-bundle-zio-http4s-blaze",
    libraryDependencies ++= Seq(
      Dependencies.zio,
      Dependencies.zioInteropCats
    )
  )

lazy val doobieHikari = project
  .in(file("doobie-hikari"))
  .settings(commonSettings)
  .settings(
    name := "sst-doobie-hikari",
    libraryDependencies ++= Seq(
      Dependencies.doobie,
      Dependencies.doobieHikari
    )
  )

lazy val doobieHikariPureConfig = project
  .in(file("doobie-hikari-pureconfig"))
  .dependsOn(doobieHikari, pureConfig)
  .settings(commonSettings)
  .settings(
    name := "sst-doobie-hikari-pureconfig"
  )

lazy val example = project
  .in(file("example"))
  .dependsOn(bundleZioHttp4sBlaze, doobieHikari, doobieHikariPureConfig, flyway, flywayPureConfig, micrometerJmxPureConfig, sslConfig)
  .enablePlugins(MdocPlugin)
  .settings(commonSettings)
  .settings(
    name := "sst-example",
    publish / skip := true,
    run / fork := true,
    Global / cancelable := true,
    mdocIn := baseDirectory.value / "mdoc",
    mdocOut := baseDirectory.value / ".." / "docs",
    libraryDependencies ++= Seq(
      Dependencies.logbackClassic,
      Dependencies.postgresql
    )
  )

lazy val flyway = project
  .in(file("flyway"))
  .settings(commonSettings)
  .settings(
    name := "sst-flyway",
    libraryDependencies += Dependencies.flywayCore
  )

lazy val flywayPureConfig = project
  .in(file("flyway-pureconfig"))
  .dependsOn(flyway)
  .settings(commonSettings)
  .settings(
    name := "sst-flyway-pureconfig",
    libraryDependencies += Dependencies.pureConfig
  )

lazy val http4sClientBlaze = project
  .in(file("http4s-client-blaze"))
  .settings(commonSettings)
  .settings(
    name := "sst-http4s-client-blaze",
    libraryDependencies += Dependencies.http4sBlazeClient
  )

lazy val http4sClientBlazePureConfig = project
  .in(file("http4s-client-blaze-pureconfig"))
  .dependsOn(http4sClientBlaze, jvmPureConfig)
  .settings(commonSettings)
  .settings(name := "sst-http4s-client-blaze-pureconfig")

lazy val http4sClientMonixCatnapMicrometer = project
  .in(file("http4s-client-monix-catnap-micrometer"))
  .dependsOn(monixCatnapMicrometer)
  .settings(commonSettings)
  .settings(
    name := "sst-http4s-client-monix-catnap-micrometer",
    libraryDependencies ++= Seq(
      Dependencies.http4sClient,
      Dependencies.slf4jApi
    )
  )

lazy val http4sServer = project
  .in(file("http4s-server"))
  .settings(commonSettings)
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
  .settings(commonSettings)
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
  .dependsOn(http4sServerBlaze, pureConfig)
  .settings(commonSettings)
  .settings(name := "sst-http4s-server-blaze-pureconfig")

lazy val http4sServerMicrometer = project
  .in(file("http4s-server-micrometer"))
  .dependsOn(http4sServer)
  .settings(commonSettings)
  .settings(
    name := "sst-http4s-server-micrometer",
    libraryDependencies += Dependencies.micrometerCore
  )

lazy val jvm = project
  .in(file("jvm"))
  .settings(commonSettings)
  .settings(
    name := "sst-jvm",
    libraryDependencies += Dependencies.slf4jApi
  )

lazy val jvmMicrometer = project
  .in(file("jvm-micrometer"))
  .dependsOn(jvm)
  .settings(commonSettings)
  .settings(
    name := "sst-jvm-micrometer",
    libraryDependencies += Dependencies.micrometerCore
  )

lazy val jvmPureConfig = project
  .in(file("jvm-pureconfig"))
  .dependsOn(jvm, pureConfig)
  .settings(commonSettings)
  .settings(name := "sst-jvm-pureconfig")

lazy val micrometerJmx = project
  .in(file("micrometer-jmx"))
  .settings(commonSettings)
  .settings(
    name := "sst-micrometer-jmx",
    libraryDependencies ++= Seq(
      Dependencies.micrometerJmx,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

lazy val micrometerJmxPureConfig = project
  .in(file("micrometer-jmx-pureconfig"))
  .dependsOn(micrometerJmx, pureConfig)
  .settings(commonSettings)
  .settings(name := "sst-micrometer-jmx-pureconfig")

lazy val micrometerStatsD = project
  .in(file("micrometer-statsd"))
  .settings(commonSettings)
  .settings(
    name := "sst-micrometer-statsd",
    libraryDependencies ++= Seq(
      Dependencies.micrometerStatsD,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

lazy val micrometerStatsDPureConfig = project
  .in(file("micrometer-statsd-pureconfig"))
  .dependsOn(micrometerStatsD, pureConfig)
  .settings(commonSettings)
  .settings(name := "sst-micrometer-statsd-pureconfig")

lazy val monixCatnap = project
  .in(file("monix-catnap"))
  .settings(commonSettings)
  .settings(
    name := "sst-monix-catnap",
    libraryDependencies += Dependencies.monixCatnap
  )

lazy val monixCatnapMicrometer = project
  .in(file("monix-catnap-micrometer"))
  .dependsOn(monixCatnap)
  .settings(commonSettings)
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
  .settings(commonSettings)
  .settings(
    name := "sst-monix-catnap-pureconfig",
    libraryDependencies += Dependencies.pureConfig
  )

lazy val pureConfig = project
  .in(file("pureconfig"))
  .settings(commonSettings)
  .settings(
    name := "sst-pureconfig",
    libraryDependencies += Dependencies.pureConfig
  )

lazy val sslConfig = project
  .in(file("ssl-config"))
  .settings(commonSettings)
  .settings(
    name := "sst-ssl-config",
    libraryDependencies ++= Seq(
      Dependencies.slf4jApi,
      Dependencies.sslConfig
    )
  )

addCommandAlias("check", "; scalafmtSbtCheck; scalafmtCheckAll; compile:scalafix --check; test:scalafix --check")
addCommandAlias("fix", "; scalafmtSbt; scalafmtAll; compile:scalafix; test:scalafix; example/mdoc")
