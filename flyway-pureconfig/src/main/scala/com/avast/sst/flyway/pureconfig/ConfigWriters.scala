package com.avast.sst.flyway.pureconfig

import com.avast.sst.flyway.FlywayConfig
import org.flywaydb.core.api.MigrationVersion
import pureconfig.ConfigWriter
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveWriter

import java.nio.charset.Charset

trait ConfigWriters {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit private[pureconfig] val flywayCharsetWriter: ConfigWriter[Charset] = ConfigWriter[String].contramap[Charset](_.name)

  implicit val flywayMigrationVersionWriter: ConfigWriter[MigrationVersion] = ConfigWriter[String].contramap[MigrationVersion](_.getVersion)

  implicit val flywayFlywayConfigWriter: ConfigWriter[FlywayConfig] = deriveWriter
}
