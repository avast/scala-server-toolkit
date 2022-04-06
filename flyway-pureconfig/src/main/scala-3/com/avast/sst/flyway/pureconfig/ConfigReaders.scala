package com.avast.sst.flyway.pureconfig

import cats.syntax.either._
import com.avast.sst.flyway.FlywayConfig
import org.flywaydb.core.api.MigrationVersion
import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown

import java.nio.charset.Charset

trait ConfigReaders {

  implicit private[pureconfig] val flywayCharsetReader: ConfigReader[Charset] = ConfigReader[String].emap { value =>
    Either.catchNonFatal(Charset.forName(value)).leftMap(ExceptionThrown.apply)
  }

  implicit val flywayMigrationVersionReader: ConfigReader[MigrationVersion] = ConfigReader[String].map(MigrationVersion.fromVersion)

  implicit val flywayFlywayConfigReader: ConfigReader[FlywayConfig] = implicitly[ConfigReader[FlywayConfig]]

}
