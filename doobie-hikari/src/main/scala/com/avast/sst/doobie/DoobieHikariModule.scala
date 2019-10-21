package com.avast.sst.doobie

import cats.effect.{Async, Blocker, ContextShift, Resource, Sync}
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.metrics.MetricsTrackerFactory
import doobie.hikari.HikariTransactor

import scala.concurrent.ExecutionContext

object DoobieHikariModule {

  /** Makes [[doobie.hikari.HikariTransactor]] initialized with the given config.
    *
    * @param boundedConnectExecutionContext [[scala.concurrent.ExecutionContext]] used for creating connections (should be bounded!)
    */
  def make[F[_]: Async](config: DoobieHikariConfig,
                        boundedConnectExecutionContext: ExecutionContext,
                        blocker: Blocker,
                        metricsTrackerFactory: MetricsTrackerFactory)(implicit cs: ContextShift[F]): Resource[F, HikariTransactor[F]] = {
    for {
      hikariConfig <- Resource.liftF {
                       Sync[F].delay {
                         val c = new HikariConfig()
                         c.setDriverClassName(config.driver)
                         c.setJdbcUrl(config.url)
                         c.setUsername(config.username)
                         c.setPassword(config.password)
                         c.setAutoCommit(config.autoCommit)
                         c.setConnectionTimeout(config.connectionTimeout.toMillis)
                         c.setIdleTimeout(config.idleTimeout.toMillis)
                         c.setMaxLifetime(config.maxLifeTime.toMillis)
                         c.setMinimumIdle(config.minimumIdle)
                         c.setMaximumPoolSize(config.maximumPoolSize)
                         c.setReadOnly(config.readOnly)
                         c.setLeakDetectionThreshold(config.leakDetectionThreshold.map(_.toMillis).getOrElse(0))
                         c.setMetricsTrackerFactory(metricsTrackerFactory)
                         c
                       }
                     }
      transactor <- HikariTransactor.fromHikariConfig(hikariConfig, boundedConnectExecutionContext, blocker)
    } yield transactor
  }

}
