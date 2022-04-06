package com.avast.sst.http4s.client.pureconfig

import cats.syntax.either._
import com.avast.sst.http4s.client.Http4sBlazeClientConfig
import org.http4s.blaze.client.ParserMode
import org.http4s.headers.`User-Agent`
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert

trait ConfigReaders {

  implicit val http4sClientUserAgentReader: ConfigReader[`User-Agent`] = ConfigReader[String].emap { value =>
    `User-Agent`.parse(value).leftMap { parseFailure => CannotConvert(value, "User-Agent HTTP header", parseFailure.message) }
  }

  implicit val http4sClientParserModeReader: ConfigReader[ParserMode] = implicitly[ConfigReader[ParserMode]]

  implicit val http4sClientHttp4sBlazeClientConfigReader: ConfigReader[Http4sBlazeClientConfig] =
    implicitly[ConfigReader[Http4sBlazeClientConfig]]

}
