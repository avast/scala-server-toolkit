import sbt.Keys._
import sbt._
import scalafix.sbt.ScalafixPlugin.autoImport._
import wartremover.WartRemover.autoImport._

object BuildSettings {

  lazy val common = Seq(
    ThisBuild / scalaVersion := "2.12.10",
    libraryDependencies ++= Seq(
      compilerPlugin(Dependencies.kindProjector),
      compilerPlugin(Dependencies.silencer),
      compilerPlugin(scalafixSemanticdb), // for scalafix
      Dependencies.silencerLib
    ),
    Compile / compile / wartremoverErrors ++= Warts.all filterNot Set(
      Wart.Null, // checked by scalafix
      Wart.Nothing, // keep, false positives all around
      Wart.Overloading,
      Wart.Any, // keep, false positives all around
      Wart.Equals, // checked by scalafix
      Wart.ToString, // checked by scalafix
      Wart.Product, // checked by scalafix
      Wart.Serializable, // checked by scalafix
      Wart.DefaultArguments // for constructors for PureConfig
    ),
    ThisBuild / scalafixDependencies ++= Seq(
      Dependencies.scalazzi,
      Dependencies.sortImports
    ),
    scalacOptions ++= Seq(
      "-Yrangepos", // for scalafix. required by SemanticDB compiler plugin
      "-Ywarn-unused", // for scalafix. not present in sbt-tpolecat for 2.13
      "-P:silencer:checkUnused"
    ),
    Test / publishArtifact := false,
    Test / test / wartremoverErrors := (Compile / compile / wartremoverErrors).value filterNot Set(
      Wart.MutableDataStructures,
      Wart.OptionPartial,
      Wart.AsInstanceOf,
      Wart.EitherProjectionPartial
    )
  )

}
