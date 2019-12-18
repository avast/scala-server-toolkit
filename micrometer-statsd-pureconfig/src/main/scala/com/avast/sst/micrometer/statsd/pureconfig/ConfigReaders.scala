package com.avast.sst.micrometer.statsd.pureconfig

import com.avast.sst.micrometer.statsd.MicrometerStatsDConfig
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val micrometerMicrometerStatsDConfigReader: ConfigReader[MicrometerStatsDConfig] = deriveReader

}
