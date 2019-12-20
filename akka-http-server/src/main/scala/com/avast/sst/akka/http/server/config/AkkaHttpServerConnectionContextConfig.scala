package com.avast.sst.akka.http.server.config

import akka.stream.TLSClientAuth
import com.typesafe.config.{Config, ConfigFactory}

sealed trait AkkaHttpServerConnectionContextConfig

case object AkkaHttpServerConnectionContextConfig {
  case object NoEncryption extends AkkaHttpServerConnectionContextConfig
  final case class Https(enabledCipherSuites: Option[Seq[String]] = None,
                         enabledProtocols: Option[Seq[String]] = None,
                         clientAuth: Option[TLSClientAuth] = None,
                         sslContext: Config = ConfigFactory.empty(), // To be used by SSL Config Module
                         sslParameters: Config = ConfigFactory.empty()) // To be used by SSL Config Module
      extends AkkaHttpServerConnectionContextConfig
}
