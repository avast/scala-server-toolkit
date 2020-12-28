import com.typesafe.sbt.site.SitePlugin.autoImport._
import dotty.tools.sbtplugin.DottyPlugin.autoImport._
import mdoc.MdocPlugin.autoImport._
import microsites.CdnDirectives
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt._
import sbt.nio.Keys._
import sbtunidoc.ScalaUnidocPlugin.autoImport._
import scalafix.sbt.ScalafixPlugin.autoImport._

object BuildSettings {

  lazy val common: Seq[Def.Setting[_]] = List(
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    Global / cancelable := true,
    Global / excludeLintKeys += fork,
    turbo := true,
    organization := "com.avast",
    organizationName := "Avast",
    organizationHomepage := Some(url("https://avast.com")),
    homepage := Some(url("https://github.com/avast/scala-server-toolkit")),
    description := "Functional programming toolkit for building server applications in Scala.",
    licenses := List("MIT" -> url("https://raw.githubusercontent.com/avast/scala-server-toolkit/master/LICENSE")),
    developers := List(Developer("jakubjanecek", "Jakub Janecek", "janecek@avast.com", url("https://www.avast.com"))),
    scalaVersion := "2.13.3",
    crossScalaVersions := List(scalaVersion.value, "2.12.12", "3.0.0-M3"),
    fork := true,
    libraryDependencies ++= {
      if (!isDotty.value) List(compilerPlugin(Dependencies.kindProjector)) else Nil
    },
    libraryDependencies ++= List(
      Dependencies.catsEffect.withDottyCompat(scalaVersion.value),
      Dependencies.scalaCollectionCompat.withDottyCompat(scalaVersion.value),
      Dependencies.logbackClassic % Test,
      Dependencies.scalaTest.withDottyCompat(scalaVersion.value) % Test
    ),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    ThisBuild / scalafixDependencies ++= List(
      Dependencies.scalafixScaluzzi,
      Dependencies.scalafixSortImports
    ),
    scalacOptions ++= {
      if (isDotty.value) List("-source:3.0-migration")
      else
        List("-Ywarn-unused" /* necessary for Scalafix RemoveUnused rule (not present in sbt-tpolecat for 2.13) */ )
    },
    scalacOptions := {
      if (scalaVersion.value.startsWith("2.12")) scalacOptions.value.filterNot(_ == "-Xfatal-warnings") else scalacOptions.value
    },
    Test / publishArtifact := false
  )

  lazy val microsite: Seq[Def.Setting[_]] = List(
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
