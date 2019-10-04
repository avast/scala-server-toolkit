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
  .aggregate(example, http4sBlazeClient, http4sBlazeServer, jvmExecution, jvmSsl, jvmSystem, pureconfig)
  .settings(
    name := "scala-server-toolkit",
    publish / skip := true
  )

lazy val example = project
  .dependsOn(jvmExecution, http4sBlazeClient, http4sBlazeServer, jvmSsl, jvmSystem, pureconfig)
  .enablePlugins(MdocPlugin)
  .settings(commonSettings)
  .settings(
    name := "sst-example",
    publish / skip := true,
    run / fork := true,
    Global / cancelable := true,
    mdocIn := baseDirectory.value / "src" / "main" / "mdoc",
    mdocOut := baseDirectory.value / ".." / "docs",
    libraryDependencies ++= Seq(
      Dependencies.zio,
      Dependencies.zioInteropCats
    )
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
  .settings(
    commonSettings,
    name := "sst-jvm-execution",
    libraryDependencies += Dependencies.slf4jApi
  )

lazy val jvmSsl = project
  .in(file("jvm-ssl"))
  .settings(
    commonSettings,
    name := "sst-jvm-ssl"
  )

lazy val jvmSystem = project
  .in(file("jvm-system"))
  .settings(
    commonSettings,
    name := "sst-jvm-system"
  )

lazy val pureconfig = project
  .settings(commonSettings)
  .settings(
    name := "sst-pureconfig",
    libraryDependencies += Dependencies.pureConfig
  )

addCommandAlias("check", "; scalafmtSbtCheck; scalafmtCheckAll; compile:scalafix --check; test:scalafix --check")
addCommandAlias("fix", "; scalafmtSbt; scalafmtAll; compile:scalafix; test:scalafix; example/mdoc")
