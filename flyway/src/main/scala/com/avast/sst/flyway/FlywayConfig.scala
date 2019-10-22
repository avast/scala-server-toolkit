package com.avast.sst.flyway

import java.nio.charset.{Charset, StandardCharsets}

import org.flywaydb.core.api.MigrationVersion

final case class FlywayConfig(baselineOnMigrate: Boolean = false,
                              baselineVersion: Option[MigrationVersion] = None,
                              baselineDescription: Option[String] = None,
                              batch: Boolean = false,
                              cleanDisabled: Boolean = false,
                              cleanOnValidationError: Boolean = false,
                              connectRetries: Int = 0,
                              encoding: Charset = StandardCharsets.UTF_8,
                              group: Boolean = false,
                              ignoreFutureMigrations: Boolean = true,
                              ignoreIgnoredMigrations: Boolean = false,
                              ignoreMissingMigrations: Boolean = false,
                              ignorePendingMigrations: Boolean = false,
                              installedBy: Option[String] = None,
                              mixed: Boolean = false,
                              locations: List[String] = List.empty,
                              outOfOrder: Boolean = false,
                              validateOnMigrate: Boolean = true,
                              licenseKey: Option[String] = None)
