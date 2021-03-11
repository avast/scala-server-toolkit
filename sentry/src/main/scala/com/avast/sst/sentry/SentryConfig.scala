package com.avast.sst.sentry

final case class SentryConfig(
    dsn: Option[String] = None,
    release: Option[String] = None,
    environment: Option[String] = None,
    distribution: Option[String] = None,
    serverName: Option[String] = None,
    inAppInclude: List[String] = List.empty
)
