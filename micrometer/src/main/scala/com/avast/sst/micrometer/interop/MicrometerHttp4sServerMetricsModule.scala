package com.avast.sst.micrometer.interop

import cats.effect.{Clock, Effect, Sync}
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.HttpRoutes
import org.http4s.server.middleware.Metrics

import scala.language.higherKinds

class MicrometerHttp4sServerMetricsModule[F[_]: Sync](val globalMetrics: HttpRoutes[F] => HttpRoutes[F],
                                                      val routeMetrics: Http4sRouteMetrics[F])

object MicrometerHttp4sServerMetricsModule {

  def apply[F[_]: Effect](meterRegistry: MeterRegistry, clock: Clock[F]): MicrometerHttp4sServerMetricsModule[F] = {
    implicit val c: Clock[F] = clock
    new MicrometerHttp4sServerMetricsModule[F](Metrics(new Http4sServerMetrics[F](meterRegistry)),
                                               new Http4sRouteMetrics[F](meterRegistry, clock))
  }

}
