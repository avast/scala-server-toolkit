package com.avast.sst.http4s.client.pureconfig.ember

import cats.syntax.either.*
import com.avast.sst.http4s.client.Http4sEmberClientConfig
import com.avast.sst.http4s.client.Http4sEmberClientConfig.SocketOptions
import org.http4s.headers.`User-Agent`
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.*

trait ConfigReaders {
  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val http4sClientUserAgentReader: ConfigReader[`User-Agent`] = ConfigReader[String].emap { value =>
    `User-Agent`.parse(value).leftMap { parseFailure => CannotConvert(value, "User-Agent HTTP header", parseFailure.message) }
  }

  implicit val http4sClientSocketOptionsReader: ConfigReader[SocketOptions] = deriveReader[SocketOptions]

  implicit val http4sClientHttp4sEmberClientConfigReader: ConfigReader[Http4sEmberClientConfig] = deriveReader[Http4sEmberClientConfig]
}
