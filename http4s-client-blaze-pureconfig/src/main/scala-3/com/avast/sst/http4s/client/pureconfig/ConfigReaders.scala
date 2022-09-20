package com.avast.sst.http4s.client.pureconfig

import cats.syntax.either.*
import com.avast.sst.http4s.client.Http4sBlazeClientConfig
import com.avast.sst.http4s.client.Http4sBlazeClientConfig.SocketOptions
import org.http4s.blaze.client.ParserMode
import org.http4s.headers.`User-Agent`
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.derivation.default.*

trait ConfigReaders {

  implicit val http4sClientUserAgentReader: ConfigReader[`User-Agent`] = ConfigReader[String].emap { value =>
    `User-Agent`.parse(value).leftMap { parseFailure => CannotConvert(value, "User-Agent HTTP header", parseFailure.message) }
  }

  implicit val http4sClientSocketOptionsReader: ConfigReader[SocketOptions] = ConfigReader.derived

  implicit val http4sClientParserModeReader: ConfigReader[ParserMode] = ConfigReader.derived

  implicit val http4sClientHttp4sBlazeClientConfigReader: ConfigReader[Http4sBlazeClientConfig] =
    ConfigReader.derived

}
