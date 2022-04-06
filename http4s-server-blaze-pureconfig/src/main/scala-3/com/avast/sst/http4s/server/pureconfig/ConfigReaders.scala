package com.avast.sst.http4s.server.pureconfig

import com.avast.sst.http4s.server.Http4sBlazeServerConfig
import com.avast.sst.http4s.server.Http4sBlazeServerConfig.SocketOptions
import pureconfig.ConfigReader

trait ConfigReaders {

  implicit val http4sServerSocketOptionsReader: ConfigReader[SocketOptions] = implicitly[ConfigReader[SocketOptions]]

  implicit val http4sServerHttp4sBlazeServerConfigReader: ConfigReader[Http4sBlazeServerConfig] =
    implicitly[ConfigReader[Http4sBlazeServerConfig]]

}
