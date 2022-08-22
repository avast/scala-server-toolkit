package com.avast.sst.http4s.server.pureconfig.ember

import com.avast.sst.http4s.server.Http4sEmberServerConfig
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.*

trait ConfigReaders {

  implicit val http4sServerHttp4sEmberServerConfigReader: ConfigReader[Http4sEmberServerConfig] =
    ConfigReader.derived

}
