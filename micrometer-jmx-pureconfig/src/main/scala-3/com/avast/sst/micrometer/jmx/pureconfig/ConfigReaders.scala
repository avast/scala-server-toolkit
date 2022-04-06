package com.avast.sst.micrometer.jmx.pureconfig

import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import pureconfig.ConfigReader

trait ConfigReaders {

  implicit val micrometerMicrometerJmxConfigReader: ConfigReader[MicrometerJmxConfig] = implicitly[ConfigReader[MicrometerJmxConfig]]

}
