package com.avast.sst.http4s.server.pureconfig

import com.avast.sst.http4s.server.Http4sBlazeServerConfig
import com.avast.sst.http4s.server.Http4sBlazeServerConfig.SocketOptions
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default._

trait ConfigReaders {

  implicit val http4sServerSocketOptionsReader: ConfigReader[SocketOptions] = ConfigReader.derived

  implicit val http4sServerHttp4sBlazeServerConfigReader: ConfigReader[Http4sBlazeServerConfig] =
    ConfigReader.derived

}
