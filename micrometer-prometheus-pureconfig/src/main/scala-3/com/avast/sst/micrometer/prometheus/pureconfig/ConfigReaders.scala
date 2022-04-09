package com.avast.sst.micrometer.prometheus.pureconfig

import com.avast.sst.micrometer.prometheus.MicrometerPrometheusConfig
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default._

trait ConfigReaders {

  implicit val micrometerPrometheusConfigReader: ConfigReader[MicrometerPrometheusConfig] =
    ConfigReader.derived

}
