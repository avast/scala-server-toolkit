package com.avast.sst.micrometer.statsd.pureconfig

import com.avast.sst.micrometer.statsd.MicrometerStatsDConfig
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.*

trait ConfigReaders {

  implicit val micrometerMicrometerStatsDConfigReader: ConfigReader[MicrometerStatsDConfig] =
    ConfigReader.derived

}
