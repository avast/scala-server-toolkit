package com.avast.sst.flyway

final case class FlywayConfig(url: String, username: String, password: String, baselineOnMigrate: Boolean = false)
