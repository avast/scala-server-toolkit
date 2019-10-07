package com.avast.sst.pureconfig.implicits

import com.avast.sst.ssl.KeyStoreType.{JKS, PKCS12}
import com.avast.sst.ssl.Protocol.{SSL, TLS}
import com.avast.sst.ssl.{KeyStoreConfig, KeyStoreType, Protocol, SslContextConfig}
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.semiauto.deriveReader

object JvmSsl {

  implicit val sslProtocolReader: ConfigReader[Protocol] = ConfigReader[String].map(_.toLowerCase).emap {
    case "tls"    => Right(TLS)
    case "ssl"    => Right(SSL)
    case badValue => Left(CannotConvert(badValue, "SSL Protocol", "TLS|SSL"))
  }

  implicit val keyStoreTypeReader: ConfigReader[KeyStoreType] = ConfigReader[String].map(_.toLowerCase).emap {
    case "jks"    => Right(JKS)
    case "pkcs12" => Right(PKCS12)
    case badValue => Left(CannotConvert(badValue, "Keystore Type", "JKS|PKCS12"))
  }

  implicit val keyStoreConfigReader: ConfigReader[KeyStoreConfig] = deriveReader

  implicit val sslContextConfigReader: ConfigReader[SslContextConfig] = deriveReader

}
