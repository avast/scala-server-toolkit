package com.avast.sst.micrometer.jmx.pureconfig

import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit val micrometerMicrometerJmxConfigReader: ConfigReader[MicrometerJmxConfig] = deriveReader

}
