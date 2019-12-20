package com.avast.sst.akka.http.server.config

import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration._

final case class AkkaHttpServerConfig(
  listenHostname: String = "0.0.0.0",
  listenPort: Int = 8008,
  connectionContext: AkkaHttpServerConnectionContextConfig = AkkaHttpServerConnectionContextConfig.NoEncryption,
  startupTimeout: FiniteDuration = 30.seconds,
  shutdownTimeout: FiniteDuration = 30.seconds,
  actorSystem: Config = ConfigFactory.empty() // To be used by Akka Actor System Module
)
