package com.avast.sst.flyway.pureconfig

import java.nio.charset.{Charset, IllegalCharsetNameException, UnsupportedCharsetException}

import cats.syntax.either._
import com.avast.sst.flyway.FlywayConfig
import org.flywaydb.core.api.MigrationVersion
import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit private[pureconfig] val charsetReader: ConfigReader[Charset] = ConfigReader[String].emap { value =>
    try {
      Charset.forName(value).asRight
    } catch {
      case ex @ (_: IllegalArgumentException | _: IllegalCharsetNameException | _: UnsupportedCharsetException) =>
        ExceptionThrown(ex).asLeft
    }
  }

  implicit val migrationVersionReader: ConfigReader[MigrationVersion] = ConfigReader[String].map(MigrationVersion.fromVersion)

  implicit val configReader: ConfigReader[FlywayConfig] = deriveReader

}
