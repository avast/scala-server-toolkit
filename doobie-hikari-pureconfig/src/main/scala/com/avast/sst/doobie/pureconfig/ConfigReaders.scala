package com.avast.sst.doobie.pureconfig

import com.avast.sst.doobie.DoobieHikariConfig
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit val configReader: ConfigReader[DoobieHikariConfig] = deriveReader

}
