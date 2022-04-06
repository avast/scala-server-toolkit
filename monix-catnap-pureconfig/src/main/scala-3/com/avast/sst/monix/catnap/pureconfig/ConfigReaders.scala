package com.avast.sst.monix.catnap.pureconfig

import com.avast.sst.monix.catnap.CircuitBreakerConfig
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default._

trait ConfigReaders {

  implicit val monixCatnapCircuitBreakerConfigReader: ConfigReader[CircuitBreakerConfig] = ConfigReader.derived

}
