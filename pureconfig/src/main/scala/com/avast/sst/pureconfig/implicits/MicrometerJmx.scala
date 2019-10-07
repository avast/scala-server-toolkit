package com.avast.sst.pureconfig.implicits

import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

object MicrometerJmx {

  implicit val jmxConfigReader: ConfigReader[MicrometerJmxConfig] = deriveReader

}
