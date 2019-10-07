package com.avast.sst.micrometer.interop

import cats.effect.{Clock, Effect, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.HttpRoutes
import org.http4s.server.middleware.Metrics

import scala.language.higherKinds

class MicrometerHttp4sServerMetricsModule[F[_]: Sync](val globalMetrics: HttpRoutes[F] => HttpRoutes[F],
                                                      val routeMetrics: Http4sRouteMetrics[F])

object MicrometerHttp4sServerMetricsModule {

  def make[F[_]: Effect](meterRegistry: MeterRegistry, clock: Clock[F]): F[MicrometerHttp4sServerMetricsModule[F]] = {
    implicit val c: Clock[F] = clock

    for {
      metricsOps <- Http4sMetricsOps.make[F](meterRegistry)
      routeMetrics <- Sync[F].delay(new Http4sRouteMetrics[F](meterRegistry, clock))
    } yield new MicrometerHttp4sServerMetricsModule[F](Metrics(metricsOps), routeMetrics)
  }

}
