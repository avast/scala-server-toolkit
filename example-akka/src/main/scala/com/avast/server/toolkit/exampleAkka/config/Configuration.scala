package com.avast.server.toolkit.exampleAkka.config

import com.avast.server.toolkit.akkahttp.AkkaHttpServerConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto._

final case class Configuration(akka: AkkaConfiguration)

final case class AkkaConfiguration(http: AkkaHttpServerConfig)

object Configuration {

  implicit val reader: ConfigReader[Configuration] = deriveReader
  implicit val readerAkka: ConfigReader[AkkaConfiguration] = deriveReader
  implicit val readerAkkaHttp: ConfigReader[AkkaHttpServerConfig] = deriveReader

}
