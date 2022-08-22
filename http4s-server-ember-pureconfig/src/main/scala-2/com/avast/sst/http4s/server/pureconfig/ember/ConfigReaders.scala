package com.avast.sst.http4s.server.pureconfig.ember

import com.avast.sst.http4s.server.Http4sEmberServerConfig
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {
  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val http4sServerHttp4sEmberServerConfigReader: ConfigReader[Http4sEmberServerConfig] = deriveReader[Http4sEmberServerConfig]

}
