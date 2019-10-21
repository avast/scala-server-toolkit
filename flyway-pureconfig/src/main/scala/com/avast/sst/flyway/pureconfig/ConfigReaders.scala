package com.avast.sst.flyway.pureconfig

import com.avast.sst.flyway.FlywayConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit val configReader: ConfigReader[FlywayConfig] = deriveReader

}
