package com.avast.sst.http4s.server.micrometer

import cats.effect.Sync
import cats.effect.syntax.bracket._
import cats.syntax.flatMap._
import cats.syntax.functor._
import io.micrometer.core.instrument.{MeterRegistry, Timer}
import org.http4s.Response

/** Provides the usual metrics for a single HTTP route. */
class RouteMetrics[F[_]: Sync](meterRegistry: MeterRegistry) {

  private val F = Sync[F]

  /** Wraps a single route with the usual metrics (count, times, HTTP status codes).
    *
    * @param name
    *   will be used in metric name
    */
  def wrap(name: String)(route: => F[Response[F]]): F[Response[F]] = {
    for {
      start <- F.delay(Timer.start(meterRegistry))
      response <- route.bracket(F.pure) { response =>
        F.delay(
          start.stop(
            meterRegistry
              .timer(s"http.$name", "status", s"${response.status.code}", "status-class", s"${response.status.code / 100}xx")
          )
        ).as(())
      }
    } yield response
  }

}
