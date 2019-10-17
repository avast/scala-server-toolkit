package com.avast.sst.micrometer.statsd.pureconfig

import com.avast.sst.micrometer.statsd.MicrometerStatsDConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}

trait ConfigReaders {

  implicit val micrometerStatsDFlavorReader: ConfigReader[MicrometerStatsDConfig.Flavor] = deriveEnumerationReader

  implicit val micrometerStatsDProtocolReader: ConfigReader[MicrometerStatsDConfig.Protocol] = deriveEnumerationReader

  implicit val micrometerStatsDConfigReader: ConfigReader[MicrometerStatsDConfig] = deriveReader

}
