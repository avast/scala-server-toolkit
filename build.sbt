ThisBuild / organization := "com.avast.server"
ThisBuild / homepage := Some(url("https://github.com/avast/scala-server-toolkit"))
ThisBuild / description := "Functional programming toolkit for building server applications in Scala."
ThisBuild / licenses := Seq("MIT" -> url("https://raw.githubusercontent.com/avast/scala-server-toolkit/master/LICENSE"))
ThisBuild / developers := List(Developer("jakubjanecek", "Jakub Janecek", "janecek@avast.com", url("https://www.avast.com")))
ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/avast/scala-server-toolkit"), "scm:git:git@github.com:avast/scala-server-toolkit.git")
)

ThisBuild / scalaVersion := "2.13.1"
ThisBuild / scalacOptions := ScalacOptions.default

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
  ),
  Test / publishArtifact := false
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "scala-server-toolkit",
    publish / skip := true
  )
