package com.avast.sst.micrometer.statsd.pureconfig

import com.avast.sst.micrometer.statsd.MicrometerStatsDConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit val micrometerStatsDConfigReader: ConfigReader[MicrometerStatsDConfig] = deriveReader

}
