package com.avast.sst.flyway

import cats.effect.Sync
import javax.sql.DataSource
import org.flywaydb.core.Flyway

object FlywayModule {

  /** Makes [[org.flywaydb.core.Flyway]] from the given [[javax.sql.DataSource]] and config. */
  def make[F[_]: Sync](dataSource: DataSource, config: FlywayConfig): F[Flyway] = {
    Sync[F].delay {
      val builder = Flyway
        .configure
        .dataSource(dataSource)
        .baselineOnMigrate(config.baselineOnMigrate)
        .cleanDisabled(config.cleanDisabled)
        .cleanOnValidationError(config.cleanOnValidationError)
        .connectRetries(config.connectRetries)
        .encoding(config.encoding)
        .group(config.group)
        .ignoreFutureMigrations(config.ignoreFutureMigrations)
        .ignoreIgnoredMigrations(config.ignoreIgnoredMigrations)
        .ignoreMissingMigrations(config.ignoreMissingMigrations)
        .ignorePendingMigrations(config.ignorePendingMigrations)
        .mixed(config.mixed)
        .outOfOrder(config.outOfOrder)
        .validateOnMigrate(config.validateOnMigrate)

      config.baselineVersion.foreach(builder.baselineVersion)
      config.targetVersion.foreach(builder.target)
      config.baselineDescription.foreach(builder.baselineDescription)
      config.installedBy.foreach(builder.installedBy)
      if (config.locations.nonEmpty) builder.locations(config.locations: _*)

      builder.load()
    }
  }

}
