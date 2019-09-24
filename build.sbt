addCommandAlias("check", "; scalafmtSbtCheck; scalafmtCheckAll; compile:scalafix --check ; test:scalafix --check")
addCommandAlias("fix", "; scalafmtSbt; scalafmtAll; compile:scalafix ; test:scalafix")

ThisBuild / organization := "com.avast"
ThisBuild / homepage := Some(url("https://github.com/avast/scala-server-toolkit"))
ThisBuild / description := "Functional programming toolkit for building server applications in Scala."
ThisBuild / licenses := Seq("MIT" -> url("https://raw.githubusercontent.com/avast/scala-server-toolkit/master/LICENSE"))
ThisBuild / developers := List(Developer("jakubjanecek", "Jakub Janecek", "janecek@avast.com", url("https://www.avast.com")))

ThisBuild / scalaVersion := "2.13.0"

ThisBuild / turbo := true

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    compilerPlugin(Dependencies.kindProjector),
    compilerPlugin(Dependencies.betterMonadicFor),
    compilerPlugin(Dependencies.silencer),
    compilerPlugin(scalafixSemanticdb), // for scalafix
    Dependencies.silencerLib,
    Dependencies.catsEffect,
    Dependencies.scalaTest % Test
  ),
  Compile / compile / wartremoverErrors ++= Warts.all filterNot Set(
    Wart.Nothing, // keep, false positives all around
    Wart.Overloading,
    Wart.Any, // keep, false positives all around
    Wart.Equals, // keep, easier that way
    Wart.ToString, // keep, easier that way
    Wart.Product, // keep, false positives all around
    Wart.Serializable // keep, false positives all around
  ),
  ThisBuild / scalafixDependencies ++= Seq(
    Dependencies.scalazzi // https://github.com/scalaz/scalazzi
  ),
  scalacOptions ++= Seq(
    "-Yrangepos", // for scalafix. required by SemanticDB compiler plugin
    "-Ywarn-unused", // for scalafix. not present in sbt-tpolecat for 2.13
    "-P:silencer:checkUnused"
  ),
  scalacOptions --= {
    if (!sys.env.contains("TRAVIS"))
      Seq(
        "-Xfatal-warnings" // for scala-fix https://scalacenter.github.io/scalafix/docs/rules/RemoveUnused.html
      )
    else
      Seq()
  },
  Test / publishArtifact := false,
  Test / test / wartremoverErrors := (Compile / compile / wartremoverErrors).value filterNot Set(
    Wart.MutableDataStructures,
    Wart.OptionPartial,
    Wart.AsInstanceOf,
    Wart.EitherProjectionPartial
  )
)

lazy val root = project
  .in(file("."))
  .aggregate(example, pureconfig)
  .settings(
    name := "scala-server-toolkit",
    publish / skip := true
  )

lazy val example = project
  .dependsOn(pureconfig)
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

lazy val pureconfig = project
  .settings(commonSettings)
  .settings(
    name := "scala-server-toolkit-pureconfig",
    libraryDependencies += Dependencies.pureConfig
  )
