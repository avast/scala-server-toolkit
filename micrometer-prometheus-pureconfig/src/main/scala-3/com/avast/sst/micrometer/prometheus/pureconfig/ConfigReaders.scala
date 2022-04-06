package com.avast.sst.micrometer.prometheus.pureconfig

import com.avast.sst.micrometer.prometheus.MicrometerPrometheusConfig
import pureconfig.ConfigReader

trait ConfigReaders {

  implicit val micrometerPrometheusConfigReader: ConfigReader[MicrometerPrometheusConfig] =
    implicitly[ConfigReader[MicrometerPrometheusConfig]]

}
