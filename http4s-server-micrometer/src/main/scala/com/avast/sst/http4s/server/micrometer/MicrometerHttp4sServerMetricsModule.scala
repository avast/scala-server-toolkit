package com.avast.sst.http4s.server.micrometer

import cats.effect.{Clock, Effect, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.HttpRoutes
import org.http4s.server.middleware.Metrics

import scala.language.higherKinds

class MicrometerHttp4sServerMetricsModule[F[_]: Sync](val serverMetrics: HttpRoutes[F] => HttpRoutes[F], val routeMetrics: RouteMetrics[F])

object MicrometerHttp4sServerMetricsModule {

  def make[F[_]: Effect](meterRegistry: MeterRegistry, clock: Clock[F]): F[MicrometerHttp4sServerMetricsModule[F]] = {
    implicit val c: Clock[F] = clock

    for {
      metricsOps <- MicrometerHttp4sMetricsOpsModule.make[F](meterRegistry)
      routeMetrics <- Sync[F].delay(new RouteMetrics[F](meterRegistry, clock))
    } yield new MicrometerHttp4sServerMetricsModule[F](Metrics(metricsOps), routeMetrics)
  }

}
