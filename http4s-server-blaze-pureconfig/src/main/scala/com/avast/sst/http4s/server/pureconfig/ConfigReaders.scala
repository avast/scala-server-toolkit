package com.avast.sst.http4s.server.pureconfig

import com.avast.sst.http4s.server.Http4sBlazeServerConfig
import com.avast.sst.http4s.server.Http4sBlazeServerConfig.SocketOptions
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit val socketOptionsReader: ConfigReader[SocketOptions] = deriveReader

  implicit val http4sServerConfigReader: ConfigReader[Http4sBlazeServerConfig] = deriveReader

}
