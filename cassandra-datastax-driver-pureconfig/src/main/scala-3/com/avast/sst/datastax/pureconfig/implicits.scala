package com.avast.sst.datastax.pureconfig

import pureconfig.ConfigFieldMapping

/** Contains [[pureconfig.ConfigReader]] instances with default "kebab-case" naming convention. */
object implicits extends ConfigReaders {

  /** Contains [[pureconfig.ConfigReader]] instances with "kebab-case" naming convention.
    *
    * This is alias for the default `implicits._` import.
    */
  object KebabCase extends ConfigReaders

}
