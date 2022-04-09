package com.avast.sst.micrometer.jmx.pureconfig

import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto._

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val micrometerMicrometerJmxConfigReader: ConfigReader[MicrometerJmxConfig] = deriveReader[MicrometerJmxConfig]

}
