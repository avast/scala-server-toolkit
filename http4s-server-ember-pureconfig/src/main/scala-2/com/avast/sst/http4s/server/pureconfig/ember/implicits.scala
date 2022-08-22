package com.avast.sst.http4s.server.pureconfig.ember

import pureconfig.ConfigFieldMapping
import pureconfig.generic.ProductHint

object implicits {

  /** Contains [[pureconfig.ConfigReader]] instances with "kebab-case" naming convention.
    *
    * This is alias for the default `implicits.scala._` import.
    */
  object KebabCase extends ConfigReaders

  /** Contains [[pureconfig.ConfigReader]] instances with "camelCase" naming convention. */
  object CamelCase extends ConfigReaders {
    implicit override protected def hint[T]: ProductHint[T] = ProductHint(ConfigFieldMapping(pureconfig.CamelCase, pureconfig.CamelCase))
  }
}
