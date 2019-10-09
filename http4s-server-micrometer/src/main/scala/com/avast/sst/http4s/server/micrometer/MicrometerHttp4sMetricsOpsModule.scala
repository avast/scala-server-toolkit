package com.avast.sst.http4s.server.micrometer

import java.util.concurrent.TimeUnit

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.metrics.{MetricsOps, TerminationType}
import org.http4s.{Method, Status}

object MicrometerHttp4sMetricsOpsModule {

  def make[F[_]: Sync](meterRegistry: MeterRegistry): F[MetricsOps[F]] = {
    val F = Sync[F]

    F.delay {
      new MetricsOps[F] {
        private val prefix = "http.global"
        private val activeRequests = meterRegistry.counter(s"$prefix.active-requests")
        private val headersTime = meterRegistry.timer(s"$prefix.headers-time")
        private val totalTime = meterRegistry.timer(s"$prefix.total-time")
        private val failureTime = meterRegistry.timer(s"$prefix.failure-time")
        private val httpStatusCodes = new HttpStatusMetrics(prefix, meterRegistry)

        override def increaseActiveRequests(classifier: Option[String]): F[Unit] = F.delay(activeRequests.increment())

        override def decreaseActiveRequests(classifier: Option[String]): F[Unit] = F.delay(activeRequests.increment(-1))

        override def recordHeadersTime(method: Method, elapsed: Long, classifier: Option[String]): F[Unit] = {
          F.delay(headersTime.record(elapsed, TimeUnit.NANOSECONDS))
        }

        override def recordTotalTime(method: Method, status: Status, elapsed: Long, classifier: Option[String]): F[Unit] = {
          for {
            _ <- F.delay(totalTime.record(elapsed, TimeUnit.NANOSECONDS))
            _ <- F.delay(httpStatusCodes.recordHttpStatus(status.code))
          } yield ()
        }

        override def recordAbnormalTermination(elapsed: Long, terminationType: TerminationType, classifier: Option[String]): F[Unit] = {
          F.delay(failureTime.record(elapsed, TimeUnit.NANOSECONDS))
        }

      }
    }
  }

}
