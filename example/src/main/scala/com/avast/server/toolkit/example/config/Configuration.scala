package com.avast.server.toolkit.example.config

import pureconfig.ConfigReader
import pureconfig.generic.semiauto._

final case class Configuration()

object Configuration {

  implicit val reader: ConfigReader[Configuration] = deriveReader

}
