package com.avast.sst.example.config

import com.avast.sst.http4s.Http4sBlazeServerConfig
import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import com.avast.sst.pureconfig.implicits.Http4sBlazeServer._
import com.avast.sst.pureconfig.implicits.MicrometerJmx.jmxConfigReader
import pureconfig.ConfigReader
import pureconfig.generic.semiauto._

final case class Configuration(server: Http4sBlazeServerConfig, jmx: MicrometerJmxConfig)

object Configuration {

  implicit val reader: ConfigReader[Configuration] = deriveReader

}
