package com.avast.sst.pureconfig.implicits

import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

/** Implicit [[pureconfig.ConfigReader]] instances for `sst-micrometer-jmx` module.
  *
  * ```Do not forget``` to have a dependency on the `sst-micrometer-jmx` module in your project.
  */
object MicrometerJmx {

  implicit val jmxConfigReader: ConfigReader[MicrometerJmxConfig] = deriveReader

}
