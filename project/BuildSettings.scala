import com.typesafe.sbt.site.SitePlugin.autoImport._
import mdoc.MdocPlugin.autoImport._
import microsites.CdnDirectives
import microsites.MicrositesPlugin.autoImport._
import sbt.Keys._
import sbt.{Def, _}
import sbtunidoc.ScalaUnidocPlugin.autoImport._
import scalafix.sbt.ScalafixPlugin.autoImport._

object BuildSettings {

  lazy val common: Seq[Def.Setting[_]] = Seq(
    crossScalaVersions := List(scalaVersion.value, "2.12.10"),
    fork := true,
    libraryDependencies ++= Seq(
      compilerPlugin(Dependencies.kindProjector),
      compilerPlugin(Dependencies.silencer),
      compilerPlugin(scalafixSemanticdb), // necessary for Scalafix
      Dependencies.silencerLib,
      Dependencies.catsEffect,
      Dependencies.logbackClassic % Test,
      Dependencies.scalaTest % Test
    ),
    ThisBuild / scalafixDependencies ++= Seq(
      Dependencies.scalafixScaluzzi,
      Dependencies.scalafixSortImports
    ),
    scalacOptions ++= Seq(
      "-Yrangepos", // necessary for Scalafix (required by SemanticDB compiler plugin)
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
