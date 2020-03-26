package com.avast.sst.sentry

final case class SentryConfig(
    dsn: String,
    release: Option[String] = None,
    environment: Option[String] = None,
    distribution: Option[String] = None,
    serverName: Option[String] = None,
    tags: Map[String, String] = Map.empty,
    stacktraceAppPackages: List[String] = List.empty
)
