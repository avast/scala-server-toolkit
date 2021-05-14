package com.avast.sst.http4s.server.micrometer

import cats.effect.Effect
import cats.syntax.functor._
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.metrics.{MetricsOps, TerminationType}
import org.http4s.{Method, Status}

import java.util.concurrent.TimeUnit
import cats.effect.Ref

object MicrometerHttp4sMetricsOpsModule {

  /** Makes [[org.http4s.metrics.MetricsOps]] to record the usual HTTP server metrics. */
  def make[F[_]: Effect](meterRegistry: MeterRegistry): F[MetricsOps[F]] = {
    val F = Effect[F]

    for {
      activeRequests <- Ref.of[F, Long](0L)
    } yield new MetricsOps[F] {

      private val prefix = "http.global"
      private val failureTime = meterRegistry.timer(s"$prefix.failure-time")

      meterRegistry.gauge(
        s"$prefix.active-requests",
        activeRequests,
        (_: Ref[F, Long]) => Effect[F].toIO(activeRequests.get).unsafeRunSync().toDouble
      )

      override def increaseActiveRequests(classifier: Option[String]): F[Unit] = activeRequests.update(_ + 1)

      override def decreaseActiveRequests(classifier: Option[String]): F[Unit] = activeRequests.update(_ - 1)

      override def recordHeadersTime(method: Method, elapsed: Long, classifier: Option[String]): F[Unit] = {
        F.delay(meterRegistry.timer(s"$prefix.headers-time", "method", method.name).record(elapsed, TimeUnit.NANOSECONDS))
      }

      override def recordTotalTime(method: Method, status: Status, elapsed: Long, classifier: Option[String]): F[Unit] = {
        F.delay(
          meterRegistry
            .timer(s"$prefix.total-time", "status", s"${status.code}", "status-class", s"${status.code / 100}xx")
            .record(elapsed, TimeUnit.NANOSECONDS)
        )
      }

      override def recordAbnormalTermination(elapsed: Long, terminationType: TerminationType, classifier: Option[String]): F[Unit] = {
        F.delay(failureTime.record(elapsed, TimeUnit.NANOSECONDS))
      }

    }
  }

}
