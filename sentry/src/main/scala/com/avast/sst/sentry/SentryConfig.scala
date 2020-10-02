package com.avast.sst.sentry

final case class SentryConfig(
    dsn: String,
    release: Option[String] = None,
    environment: Option[String] = None,
    distribution: Option[String] = None,
    serverName: Option[String] = None,
    stacktraceAppPackages: List[String] = List.empty
)
