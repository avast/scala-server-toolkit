import com.typesafe.sbt.site.SitePlugin.autoImport._
import mdoc.MdocPlugin.autoImport._
import microsites.CdnDirectives
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import sbt.nio.Keys._
import sbtunidoc.ScalaUnidocPlugin.autoImport._
import sbtversionpolicy.SbtVersionPolicyPlugin.autoImport._
import scalafix.sbt.ScalafixPlugin.autoImport._

object BuildSettings {

  private def isScala3(scalaVersion: String): Boolean = CrossVersion.partialVersion(scalaVersion).exists(_._1 == 3)

  private val scala212 = "2.12.15"
  private val scala213 = "2.13.6"
  private val scala3 = "3.1.1"

  lazy val common: Seq[Def.Setting[_]] = Seq(
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    Global / cancelable := true,
    Global / excludeLintKeys += fork,
    ThisBuild / versionScheme := Some("early-semver"),
    ThisBuild / versionPolicyIntention := Compatibility.BinaryCompatible,
    turbo := true,
    organization := "com.avast",
    organizationName := "Avast",
    organizationHomepage := Some(url("https://avast.com")),
    homepage := Some(url("https://github.com/avast/scala-server-toolkit")),
    description := "Functional programming toolkit for building server applications in Scala.",
    licenses := Seq("MIT" -> url("https://raw.githubusercontent.com/avast/scala-server-toolkit/master/LICENSE")),
    developers := List(Developer("jakubjanecek", "Jakub Janecek", "janecek@avast.com", url("https://www.avast.com"))),
    scalaVersion := scala3,
    crossScalaVersions := List(scala213, scala212, scala3),
    fork := true,
    libraryDependencies ++= (if (!isScala3(scalaVersion.value)) List(compilerPlugin(Dependencies.kindProjector)) else List.empty) ++ List(
      Dependencies.catsEffect,
      Dependencies.scalaCollectionCompat,
      "org.jetbrains" % "annotations" % "21.0.1", // TODO: this should be compile only dependency!
      Dependencies.logbackClassic % Test,
      Dependencies.scalaTest % Test
    ),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    ThisBuild / scalafixDependencies ++= Seq(
      Dependencies.scalafixScaluzzi,
      Dependencies.scalafixOrganizeImports
    ),
    scalacOptions ++=
      // necessary for Scalafix RemoveUnused rule (not present in sbt-tpolecat for 2.13))
      (if (isScala3(scalaVersion.value)) List("-source:3.0-migration") else List("-Ywarn-unused")) ++
        (if (scalaVersion.value.startsWith("2.13")) List("-Wmacros:after", "-Ytasty-reader") else List.empty),
    Compile / doc / scalacOptions -= "-Xfatal-warnings",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    Test / publishArtifact := false
  )

  lazy val microsite: Seq[Def.Setting[_]] = Seq(
    micrositeName := "scala-server-toolkit",
    micrositeDescription := "Functional programming toolkit for building server applications in Scala.",
    micrositeAuthor := "Avast",
    micrositeOrganizationHomepage := "https://avast.com",
    micrositeGithubOwner := "avast",
    micrositeGithubRepo := "scala-server-toolkit",
    micrositeUrl := "https://avast.github.io",
    micrositeDocumentationUrl := "api/latest",
    micrositeDocumentationLabelDescription := "API ScalaDoc",
    micrositeBaseUrl := "/scala-server-toolkit",
    micrositeTwitter := "@avast_devs",
    micrositeGitterChannel := false,
    micrositeTheme := "pattern",
    micrositeHighlightTheme := "github",
    micrositeCDNDirectives := CdnDirectives(
      cssList = List(
        "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.18.1/styles/github.min.css"
      )
    ),
    micrositeShareOnSocial := false,
    mdoc / fork := true,
    mdocIn := file("docs"),
    mdocVariables := Map("VERSION" -> version.value),
    mdocAutoDependency := true,
    micrositeDataDirectory := file("site"),
    ScalaUnidoc / siteSubdirName := "api/latest",
    addMappingsToSiteDir(
      ScalaUnidoc / packageDoc / mappings,
      ScalaUnidoc / siteSubdirName
    )
  )

}
