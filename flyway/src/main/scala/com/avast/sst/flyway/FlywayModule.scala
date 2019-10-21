package com.avast.sst.flyway

import cats.effect.Sync
import org.flywaydb.core.Flyway

object FlywayModule {

  def make[F[_]: Sync](config: FlywayConfig): F[Flyway] = {
    Flyway
      .configure
      .dataSource(config.url, config.username, config.password)
      .baselineOnMigrate(config.baselineOnMigrate)
      .load()
    ???
  }

}
