package com.avast.sst.pureconfig.implicits

import com.avast.sst.http4s.Http4sBlazeServerConfig
import com.avast.sst.http4s.Http4sBlazeServerConfig.SocketOptions
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

object Http4sBlazeServer {

  implicit val socketOptionsReader: ConfigReader[SocketOptions] = deriveReader

  implicit val http4sServerConfigReader: ConfigReader[Http4sBlazeServerConfig] = deriveReader

}
