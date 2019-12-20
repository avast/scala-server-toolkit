package com.avast.sst.akka.http.server.pureconfig

import akka.stream.TLSClientAuth
import com.avast.sst.akka.http.server.config.{AkkaHttpServerConfig, AkkaHttpServerConnectionContextConfig}
import pureconfig.error.CannotConvert
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto._
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigReader}

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val noEncryptionReader: ConfigReader[AkkaHttpServerConnectionContextConfig.NoEncryption.type] = ConfigReader.fromString { str =>
    if (str equalsIgnoreCase "noEncryption") Right(AkkaHttpServerConnectionContextConfig.NoEncryption)
    else Left(CannotConvert(str, "HttpConnectionContext", "Invalid value"))
  }

  implicit val tlsAuthReader: ConfigReader[TLSClientAuth] = deriveEnumerationReader(ConfigFieldMapping(CamelCase, CamelCase))

  implicit val httpsReader: ConfigReader[AkkaHttpServerConnectionContextConfig.Https] = deriveReader

  implicit val akkaHttpServerConnectionContextConfigConfigReader: ConfigReader[AkkaHttpServerConnectionContextConfig] = {
    noEncryptionReader orElse httpsReader
  }

  implicit val akkaHttpServerAkkaHttpServerConfigConfigReader: ConfigReader[AkkaHttpServerConfig] = deriveReader

}
