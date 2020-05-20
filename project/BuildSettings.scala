import com.typesafe.sbt.site.SitePlugin.autoImport._
import com.typesafe.tools.mima.plugin.MimaKeys._
import mdoc.MdocPlugin.autoImport._
import microsites.CdnDirectives
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt.nio.Keys._
import sbt.{Def, _}
import sbtunidoc.ScalaUnidocPlugin.autoImport._
import scalafix.sbt.ScalafixPlugin.autoImport._

object BuildSettings {

  lazy val common: Seq[Def.Setting[_]] = Seq(
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    Global / cancelable := true,
    turbo := true,
    organization := "com.avast",
    organizationName := "Avast",
    organizationHomepage := Some(url("https://avast.com")),
    homepage := Some(url("https://github.com/avast/scala-server-toolkit")),
    description := "Functional programming toolkit for building server applications in Scala.",
    licenses := Seq("MIT" -> url("https://raw.githubusercontent.com/avast/scala-server-toolkit/master/LICENSE")),
    developers := List(Developer("jakubjanecek", "Jakub Janecek", "janecek@avast.com", url("https://www.avast.com"))),
    scalaVersion := "2.13.2",
    crossScalaVersions := List(scalaVersion.value, "2.12.11"),
    fork := true,
    mimaPreviousArtifacts ~= { _.filter(_.revision == "0.1.34") }, // this is just temporary until we have release version 1.x
    mimaFailOnNoPrevious := false,
    libraryDependencies ++= Seq(
      compilerPlugin(Dependencies.kindProjector),
      compilerPlugin(Dependencies.silencer),
      Dependencies.silencerLib,
      Dependencies.catsEffect,
      Dependencies.scalaCollectionCompat,
      Dependencies.logbackClassic % Test,
      Dependencies.scalaTest % Test
    ),
    semanticdbEnabled := true,
    semanticdbVersion := "4.3.10", // scalafixSemanticdb.revision,
    ThisBuild / scalafixDependencies ++= Seq(
      Dependencies.scalafixScaluzzi,
      Dependencies.scalafixSortImports
    ),
    scalacOptions ++= Seq(
      "-Ywarn-unused", // necessary for Scalafix RemoveUnused rule (not present in sbt-tpolecat for 2.13)
      "-P:silencer:checkUnused"
    ),
    Test / publishArtifact := false
  )

  lazy val microsite: Seq[Def.Setting[_]] = Seq(
    micrositeCompilingDocsTool := WithMdoc,
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
