package com.avast.sst.micrometer.statsd.pureconfig

import com.avast.sst.micrometer.statsd.MicrometerStatsDConfig
import pureconfig.ConfigReader

trait ConfigReaders {

  implicit val micrometerMicrometerStatsDConfigReader: ConfigReader[MicrometerStatsDConfig] =
    implicitly[ConfigReader[MicrometerStatsDConfig]]

}
