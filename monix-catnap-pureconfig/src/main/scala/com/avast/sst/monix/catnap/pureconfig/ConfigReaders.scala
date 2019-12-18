package com.avast.sst.monix.catnap.pureconfig

import com.avast.sst.monix.catnap.CircuitBreakerConfig
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val monixCatnapCircuitBreakerConfigReader: ConfigReader[CircuitBreakerConfig] = deriveReader

}
