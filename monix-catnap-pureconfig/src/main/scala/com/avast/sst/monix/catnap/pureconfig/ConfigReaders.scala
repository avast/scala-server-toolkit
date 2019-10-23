package com.avast.sst.monix.catnap.pureconfig

import com.avast.sst.monix.catnap.CircuitBreakerConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit val monixCatnapCircuitBreakerConfigReader: ConfigReader[CircuitBreakerConfig] = deriveReader

}
