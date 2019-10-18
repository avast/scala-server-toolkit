package com.avast.sst.doobie

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration

final case class DoobieHikariConfig(driver: String,
                                    url: String,
                                    username: String,
                                    password: String,
                                    autoCommit: Boolean = true,
                                    connectionTimeout: FiniteDuration = FiniteDuration(30, TimeUnit.SECONDS),
                                    idleTimeout: FiniteDuration = FiniteDuration(10, TimeUnit.MINUTES),
                                    maxLifeTime: FiniteDuration = FiniteDuration(30, TimeUnit.MINUTES),
                                    minimumIdle: Int = 10,
                                    maximumPoolSize: Int = 10,
                                    readOnly: Boolean = false,
                                    leakDetectionThreshold: Option[FiniteDuration] = None)
