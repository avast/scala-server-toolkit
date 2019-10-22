package com.avast.sst.flyway

import cats.effect.Sync
import org.flywaydb.core.Flyway

object FlywayModule {

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  def make[F[_]: Sync](config: FlywayConfig): F[Flyway] = {
    Sync[F].delay {
      val builder = Flyway
        .configure
        .dataSource(config.url, config.username, config.password)
        .baselineOnMigrate(config.baselineOnMigrate)
        .batch(config.batch)
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
      config.baselineDescription.foreach(builder.baselineDescription)
      config.installedBy.foreach(builder.installedBy)
      if (config.locations.nonEmpty) builder.locations(config.locations: _*)

      config.licenseKey.foreach(builder.licenseKey)

      builder.load()
    }
  }

}
