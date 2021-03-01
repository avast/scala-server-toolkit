package com.avast.sst.doobie

import doobie.enumerated.TransactionIsolation

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

final case class DoobieHikariConfig(
    driver: String,
    url: String,
    username: String,
    password: String,
    connectionTimeout: FiniteDuration = FiniteDuration(30, TimeUnit.SECONDS),
    idleTimeout: FiniteDuration = FiniteDuration(10, TimeUnit.MINUTES),
    maxLifeTime: FiniteDuration = FiniteDuration(30, TimeUnit.MINUTES),
    minimumIdle: Int = 10,
    maximumPoolSize: Int = 10,
    readOnly: Boolean = false,
    leakDetectionThreshold: Option[FiniteDuration] = None,
    allowPoolSuspension: Boolean = false,
    initializationFailTimeout: Option[FiniteDuration] = None,
    isolateInternalQueries: Boolean = false,
    poolName: Option[String] = None,
    registerMBeans: Boolean = false,
    validationTimeout: Option[FiniteDuration] = None,
    transactionIsolation: Option[TransactionIsolation] = None,
    dataSourceProperties: Map[String, String] = Map.empty,
    autoCommit: Boolean = false
)
