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
    example,
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
    pureConfig
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

lazy val example = project
  .in(file("example"))
  .dependsOn(bundleZioHttp4sBlaze, micrometerJmxPureConfig)
  .enablePlugins(MdocPlugin)
  .settings(commonSettings)
  .settings(
    name := "sst-example",
    publish / skip := true,
    run / fork := true,
    Global / cancelable := true,
    mdocIn := baseDirectory.value / "src" / "main" / "mdoc",
    mdocOut := baseDirectory.value / ".." / "docs",
    libraryDependencies += Dependencies.logbackClassic
  )

lazy val http4sClientBlaze = project
  .in(file("http4s-client-blaze"))
  .dependsOn(jvm)
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

lazy val pureConfig = project
  .in(file("pureconfig"))
  .settings(commonSettings)
  .settings(
    name := "sst-pureconfig",
    libraryDependencies += Dependencies.pureConfig
  )

addCommandAlias("check", "; scalafmtSbtCheck; scalafmtCheckAll; compile:scalafix --check; test:scalafix --check")
addCommandAlias("fix", "; scalafmtSbt; scalafmtAll; compile:scalafix; test:scalafix; example/mdoc")
