package com.avast.sst.http4s.client.pureconfig.ember

import cats.syntax.either.*
import com.avast.sst.http4s.client.Http4sEmberClientConfig
import org.http4s.headers.`User-Agent`
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.derivation.default.*

trait ConfigReaders {

  implicit val http4sClientUserAgentReader: ConfigReader[`User-Agent`] = ConfigReader[String].emap { value =>
    `User-Agent`.parse(value).leftMap { parseFailure => CannotConvert(value, "User-Agent HTTP header", parseFailure.message) }
  }

  implicit val http4sClientHttp4sEmberClientConfigReader: ConfigReader[Http4sEmberClientConfig] =
    ConfigReader.derived

}
