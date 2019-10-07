package com.avast.sst.pureconfig.implicits

import com.avast.sst.ssl.{KeyStoreConfig, KeyStoreType, Protocol, SslContextConfig}
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}

/** Implicit [[pureconfig.ConfigReader]] instances for `sst-jvm-ssl` module.
  *
  * ```Do not forget``` to have a dependency on the `sst-jvm-ssl` module in your project.
  */
object JvmSsl {

  implicit val sslProtocolReader: ConfigReader[Protocol] = deriveEnumerationReader

  implicit val keyStoreTypeReader: ConfigReader[KeyStoreType] = deriveEnumerationReader

  implicit val keyStoreConfigReader: ConfigReader[KeyStoreConfig] = deriveReader

  implicit val sslContextConfigReader: ConfigReader[SslContextConfig] = deriveReader

}
