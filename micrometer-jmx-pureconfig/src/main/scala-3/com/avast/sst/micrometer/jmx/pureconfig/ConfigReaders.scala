package com.avast.sst.micrometer.jmx.pureconfig

import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default._

trait ConfigReaders {

  implicit val micrometerMicrometerJmxConfigReader: ConfigReader[MicrometerJmxConfig] = ConfigReader.derived

}
