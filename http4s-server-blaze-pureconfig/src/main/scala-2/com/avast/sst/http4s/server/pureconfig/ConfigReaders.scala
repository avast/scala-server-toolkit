package com.avast.sst.http4s.server.pureconfig

import com.avast.sst.http4s.server.Http4sBlazeServerConfig
import com.avast.sst.http4s.server.Http4sBlazeServerConfig.SocketOptions
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto._

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val http4sServerSocketOptionsReader: ConfigReader[SocketOptions] = deriveReader[SocketOptions]

  implicit val http4sServerHttp4sBlazeServerConfigReader: ConfigReader[Http4sBlazeServerConfig] = deriveReader[Http4sBlazeServerConfig]

}
