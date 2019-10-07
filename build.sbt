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
    http4sBlazeClient,
    http4sBlazeServer,
    jvmExecution,
    jvmSsl,
    jvmSystem,
    micrometer,
    micrometerJmx,
    pureconfig
  )
  .settings(
    name := "scala-server-toolkit",
    publish / skip := true
  )

lazy val bundleMonixHttp4sBlaze = project
  .in(file("bundle-monix-http4s-blaze"))
  .dependsOn(http4sBlazeClient, http4sBlazeServer, micrometer, micrometerJmx, pureconfig)
  .settings(commonSettings)
  .settings(
    name := "sst-bundle-monix-http4s-blaze",
    libraryDependencies += Dependencies.monixEval
  )

lazy val bundleZioHttp4sBlaze = project
  .in(file("bundle-zio-http4s-blaze"))
  .dependsOn(http4sBlazeClient, http4sBlazeServer, micrometer, micrometerJmx, pureconfig)
  .settings(commonSettings)
  .settings(
    name := "sst-bundle-zio-http4s-blaze",
    libraryDependencies ++= Seq(
      Dependencies.zio,
      Dependencies.zioInteropCats
    )
  )

lazy val example = project
  .dependsOn(bundleZioHttp4sBlaze, jvmExecution, jvmSystem)
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

lazy val http4sBlazeClient = project
  .in(file("http4s-blaze-client"))
  .dependsOn(jvmSsl)
  .settings(commonSettings)
  .settings(
    name := "sst-http4s-blaze-client",
    libraryDependencies += Dependencies.http4sBlazeClient
  )

lazy val http4sBlazeServer = project
  .in(file("http4s-blaze-server"))
  .dependsOn(http4sBlazeClient % Test)
  .settings(commonSettings)
  .settings(
    name := "sst-http4s-blaze-server",
    libraryDependencies ++= Seq(
      Dependencies.http4sBlazeServer,
      Dependencies.http4sDsl,
      Dependencies.slf4jApi
    )
  )

lazy val jvmExecution = project
  .in(file("jvm-execution"))
  .settings(commonSettings)
  .settings(
    name := "sst-jvm-execution",
    libraryDependencies += Dependencies.slf4jApi
  )

lazy val jvmSsl = project
  .in(file("jvm-ssl"))
  .settings(commonSettings)
  .settings(
    name := "sst-jvm-ssl"
  )

lazy val jvmSystem = project
  .in(file("jvm-system"))
  .settings(commonSettings)
  .settings(
    name := "sst-jvm-system"
  )

lazy val micrometer = project
  .in(file("micrometer"))
  .dependsOn(http4sBlazeServer % Optional)
  .settings(commonSettings)
  .settings(
    name := "sst-micrometer",
    libraryDependencies ++= Seq(
      Dependencies.micrometerCore,
      Dependencies.jsr305 // required because of Scala compiler
    )
  )

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

lazy val pureconfig = project
  .dependsOn(http4sBlazeClient % Optional,
             http4sBlazeServer % Optional,
             jvmExecution % Optional,
             jvmSsl % Optional,
             micrometerJmx % Optional)
  .settings(commonSettings)
  .settings(
    name := "sst-pureconfig",
    libraryDependencies += Dependencies.pureConfig
  )

addCommandAlias("check", "; scalafmtSbtCheck; scalafmtCheckAll; compile:scalafix --check; test:scalafix --check")
addCommandAlias("fix", "; scalafmtSbt; scalafmtAll; compile:scalafix; test:scalafix; example/mdoc")
