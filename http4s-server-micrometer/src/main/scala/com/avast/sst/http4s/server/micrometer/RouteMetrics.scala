package com.avast.sst.http4s.server.micrometer

import java.util.concurrent.TimeUnit

import cats.effect.syntax.bracket._
import cats.effect.{Clock, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.Response

import scala.language.higherKinds

/** Provides the usual metrics for single HTTP route. */
class RouteMetrics[F[_]: Sync](meterRegistry: MeterRegistry, clock: Clock[F]) {

  private val F = Sync[F]

  /** Wraps a single route with the usual metrics (requests in-flight, timer, HTTP status counts).
    *
    * @param name will be used in metric name
    */
  def wrap(name: String)(route: => F[Response[F]]): F[Response[F]] = {
    val prefix = s"http.$name"
    val activeRequests = meterRegistry.counter(s"$prefix.active-requests")
    val timer = meterRegistry.timer(s"$prefix.total-time")
    val httpStatusCodes = new HttpStatusMetrics(prefix, meterRegistry)
    for {
      start <- clock.monotonic(TimeUnit.NANOSECONDS)
      response <- F.delay(activeRequests.increment())
                   .bracket { _ =>
                     route.flatTap(response => F.delay(httpStatusCodes.recordHttpStatus(response.status)))
                   } { _ =>
                     for {
                       time <- computeTime(start)
                       _ <- F.delay(activeRequests.increment(-1))
                       _ <- F.delay(timer.record(time, TimeUnit.NANOSECONDS))
                     } yield ()
                   }
    } yield response
  }

  private def computeTime(start: Long): F[Long] = clock.monotonic(TimeUnit.NANOSECONDS).map(_ - start)

}
