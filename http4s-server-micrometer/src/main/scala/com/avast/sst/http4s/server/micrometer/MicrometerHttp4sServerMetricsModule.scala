package com.avast.sst.http4s.server.micrometer

import cats.effect.{Blocker, Clock, Effect, Sync}
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.HttpRoutes
import org.http4s.server.middleware.Metrics

class MicrometerHttp4sServerMetricsModule[F[_]](val serverMetrics: HttpRoutes[F] => HttpRoutes[F], val routeMetrics: RouteMetrics[F])

object MicrometerHttp4sServerMetricsModule {

  /** Makes [[com.avast.sst.http4s.server.micrometer.MicrometerHttp4sServerMetricsModule]] that can be used to setup monitoring of the whole
    * HTTP server and individual routes.
    */
  def make[F[_]: Effect](meterRegistry: MeterRegistry, blocker: Blocker, clock: Clock[F]): F[MicrometerHttp4sServerMetricsModule[F]] = {
    implicit val c: Clock[F] = clock

    for {
      metricsOps <- MicrometerHttp4sMetricsOpsModule.make[F](meterRegistry, blocker)
      routeMetrics <- Sync[F].delay(new RouteMetrics[F](meterRegistry))
    } yield new MicrometerHttp4sServerMetricsModule[F](Metrics(metricsOps), routeMetrics)
  }

}
