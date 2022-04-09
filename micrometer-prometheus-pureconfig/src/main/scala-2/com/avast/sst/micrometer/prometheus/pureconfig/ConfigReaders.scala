package com.avast.sst.micrometer.prometheus.pureconfig

import com.avast.sst.micrometer.prometheus.MicrometerPrometheusConfig
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val micrometerPrometheusConfigReader: ConfigReader[MicrometerPrometheusConfig] = deriveReader

}
