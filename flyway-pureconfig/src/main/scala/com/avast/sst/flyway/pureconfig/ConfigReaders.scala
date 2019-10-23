package com.avast.sst.flyway.pureconfig

import java.nio.charset.Charset

import cats.syntax.either._
import com.avast.sst.flyway.FlywayConfig
import org.flywaydb.core.api.MigrationVersion
import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit private[pureconfig] val flywayCharsetReader: ConfigReader[Charset] = ConfigReader[String].emap { value =>
    Either.catchNonFatal(Charset.forName(value)).leftMap(ExceptionThrown.apply)
  }

  implicit val flywayMigrationVersionReader: ConfigReader[MigrationVersion] = ConfigReader[String].map(MigrationVersion.fromVersion)

  implicit val flywayFlywayConfigReader: ConfigReader[FlywayConfig] = deriveReader

}
