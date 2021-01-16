package com.avast.sst.flyway

import org.flywaydb.core.api.MigrationVersion

import java.nio.charset.{Charset, StandardCharsets}

final case class FlywayConfig(
    baselineOnMigrate: Boolean = false,
    baselineVersion: Option[MigrationVersion] = None,
    targetVersion: Option[MigrationVersion] = None,
    baselineDescription: Option[String] = None,
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
    placeholderReplacement: Boolean = true,
    placeholders: Map[String, String] = Map.empty
)
