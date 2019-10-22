package com.avast.sst.flyway.pureconfig

import java.nio.charset.Charset

import cats.syntax.either._
import com.avast.sst.flyway.FlywayConfig
import org.flywaydb.core.api.MigrationVersion
import pureconfig.ConfigReader
import pureconfig.error.ExceptionThrown
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit private[pureconfig] val charsetReader: ConfigReader[Charset] = ConfigReader[String].emap { value =>
    Either.catchNonFatal(Charset.forName(value)).leftMap(ExceptionThrown.apply)
  }

  implicit val migrationVersionReader: ConfigReader[MigrationVersion] = ConfigReader[String].map(MigrationVersion.fromVersion)

  implicit val configReader: ConfigReader[FlywayConfig] = deriveReader

}
