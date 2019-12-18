package com.avast.sst.grpc.server.pureconfig

import pureconfig.ConfigFieldMapping
import pureconfig.generic.ProductHint

/** Contains [[pureconfig.ConfigReader]] instances with default "kebab-case" naming convention. */
object implicits extends ConfigReaders {

  /** Contains [[pureconfig.ConfigReader]] instances with "kebab-case" naming convention.
    *
    * This is alias for the default `implicits._` import.
    */
  object KebabCase extends ConfigReaders

  /** Contains [[pureconfig.ConfigReader]] instances with "camelCase" naming convention. */
  object CamelCase extends ConfigReaders {
    implicit override protected def hint[T]: ProductHint[T] = ProductHint(ConfigFieldMapping(pureconfig.CamelCase, pureconfig.CamelCase))
  }

}
