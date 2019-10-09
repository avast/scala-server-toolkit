package com.avast.sst.example.config

import com.avast.sst.http4s.server.Http4sBlazeServerConfig
import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import com.avast.sst.http4s.server.pureconfig._
import com.avast.sst.micrometer.jmx.pureconfig._
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

final case class Configuration(server: Http4sBlazeServerConfig, jmx: MicrometerJmxConfig)

object Configuration {

  implicit val reader: ConfigReader[Configuration] = deriveReader

}
