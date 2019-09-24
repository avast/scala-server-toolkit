ThisBuild / organization := "com.avast"
ThisBuild / homepage := Some(url("https://github.com/avast/scala-server-toolkit"))
ThisBuild / description := "Functional programming toolkit for building server applications in Scala."
ThisBuild / licenses := Seq("MIT" -> url("https://raw.githubusercontent.com/avast/scala-server-toolkit/master/LICENSE"))
ThisBuild / developers := List(Developer("jakubjanecek", "Jakub Janecek", "janecek@avast.com", url("https://www.avast.com")))

ThisBuild / scalaVersion := "2.13.0"

ThisBuild / turbo := true

lazy val commonSettings = BuildHelper.settingsCommon ++ Seq(
  libraryDependencies ++= Seq(
    Dependencies.catsEffect,
    Dependencies.Test.scalaTest % Test
  )
)

lazy val root = project
  .in(file("."))
  .aggregate(example, jvmExecution, jvmSsl, jvmSystem, pureconfig)
  .settings(
    name := "scala-server-toolkit",
    publish / skip := true
  )

lazy val example = project
  .dependsOn(jvmExecution, jvmSsl, jvmSystem, pureconfig)
  .enablePlugins(MdocPlugin)
  .settings(commonSettings)
  .settings(
    name := "scala-server-toolkit-example",
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

lazy val jvmExecution = project
  .in(file("jvm-execution"))
  .settings(
    commonSettings,
    name := "scala-server-toolkit-jvm-execution",
    libraryDependencies += Dependencies.slf4jApi
  )

lazy val jvmSsl = project
  .in(file("jvm-ssl"))
  .settings(
    commonSettings,
    name := "scala-server-toolkit-jvm-ssl"
  )

lazy val jvmSystem = project
  .in(file("jvm-system"))
  .settings(
    commonSettings,
    name := "scala-server-toolkit-jvm-system"
  )

lazy val pureconfig = project
  .settings(commonSettings)
  .settings(
    name := "scala-server-toolkit-pureconfig",
    libraryDependencies += Dependencies.pureConfig
  )

addCommandAlias("check", "; scalafmtSbtCheck; scalafmtCheckAll; compile:scalafix --check ; test:scalafix --check")
addCommandAlias("fix", "; scalafmtSbt; scalafmtAll; compile:scalafix ; test:scalafix")
