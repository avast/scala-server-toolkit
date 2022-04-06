package com.avast.sst.monix.catnap.pureconfig

import com.avast.sst.monix.catnap.CircuitBreakerConfig
import pureconfig.ConfigReader

trait ConfigReaders {

  implicit val monixCatnapCircuitBreakerConfigReader: ConfigReader[CircuitBreakerConfig] = implicitly[ConfigReader[CircuitBreakerConfig]]

}
