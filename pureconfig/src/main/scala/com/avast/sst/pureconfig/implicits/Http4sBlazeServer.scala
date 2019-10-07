package com.avast.sst.pureconfig.implicits

import com.avast.sst.http4s.Http4sBlazeServerConfig
import com.avast.sst.http4s.Http4sBlazeServerConfig.SocketOptions
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

/** Implicit [[pureconfig.ConfigReader]] instances for `sst-http4s-blaze-server` module.
  *
  * ```Do not forget``` to have a dependency on the `sst-http4s-blaze-server` module in your project.
  */
object Http4sBlazeServer {

  implicit val socketOptionsReader: ConfigReader[SocketOptions] = deriveReader

  implicit val http4sServerConfigReader: ConfigReader[Http4sBlazeServerConfig] = deriveReader

}
