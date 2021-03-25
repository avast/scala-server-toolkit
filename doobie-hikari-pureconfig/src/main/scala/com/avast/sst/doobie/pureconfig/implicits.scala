package com.avast.sst.doobie.pureconfig

import pureconfig.ConfigFieldMapping
import pureconfig.generic.ProductHint

/** Contains [[pureconfig.ConfigReader]] and [[pureconfig.ConfigWriter]] instances with default "kebab-case" naming convention. */
object implicits extends ConfigReaders with ConfigWriters {

  override implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  /** Contains [[pureconfig.ConfigReader]] and [[pureconfig.ConfigWriter]] instances with "kebab-case" naming convention.
    *
    * This is alias for the default `implicits._` import.
    */
  object KebabCase extends ConfigReaders with ConfigWriters {
    override implicit protected def hint[T]: ProductHint[T] = ProductHint.default
  }

  /** Contains [[pureconfig.ConfigReader]] and [[pureconfig.ConfigWriter]] instances with "camelCase" naming convention. */
  object CamelCase extends ConfigReaders with ConfigWriters {
    implicit override protected def hint[T]: ProductHint[T] = ProductHint(ConfigFieldMapping(pureconfig.CamelCase, pureconfig.CamelCase))
  }

}
