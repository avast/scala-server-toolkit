package com.avast.server.toolkit.akkahttp

import scala.concurrent.duration._

final case class AkkaHttpServerConfig(
  listenHostname: String,
  listenPort: Int,
  startupTimeout: FiniteDuration = 30.seconds,
  shutdownTimeout: FiniteDuration = 30.seconds,
)