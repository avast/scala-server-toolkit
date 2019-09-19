object ScalacOptions {

  val default: Seq[String] = Seq(
    "-encoding",
    "utf8",
    "-deprecation",
    "-unchecked",
    "-feature",
    "-explaintypes",
    "-Xlint",
    "-Xfatal-warnings",
    "-Xcheckinit",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard",
    "-Ypartial-unification",
    "-Ywarn-macros:after",
    "-Ybackend-parallelism",
    "4"
  )

}
