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
      Wart.Nothing, // keep, false positives all around
      Wart.Overloading,
      Wart.Any, // keep, false positives all around
      Wart.Equals, // keep, easier that way
      Wart.ToString, // keep, easier that way
      Wart.Product, // keep, false positives all around
      Wart.Serializable, // keep, false positives all around
      Wart.DefaultArguments // for constructors for PureConfig
    ),
    ThisBuild / scalafixDependencies ++= Seq(
      Dependencies.scalazzi // https://github.com/scalaz/scalazzi
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
