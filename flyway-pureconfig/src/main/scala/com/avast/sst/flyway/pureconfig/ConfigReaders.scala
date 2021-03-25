package com.avast.sst.flyway.pureconfig

import cats.syntax.either._
import com.avast.sst.flyway.FlywayConfig
import org.flywaydb.core.api.MigrationVersion
import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader

import java.nio.charset.Charset

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit private[pureconfig] val flywayCharsetReader: ConfigReader[Charset] = ConfigReader[String].emap { value =>
    Either.catchNonFatal(Charset.forName(value)).leftMap(ExceptionThrown.apply)
  }

  implicit val flywayMigrationVersionReader: ConfigReader[MigrationVersion] = ConfigReader[String].map(MigrationVersion.fromVersion)

  implicit val flywayFlywayConfigReader: ConfigReader[FlywayConfig] = deriveReader
}
