package com.avast.sst.micrometer.interop

import java.util.concurrent.TimeUnit

import cats.effect.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.avast.sst.micrometer.HttpStatusMetrics
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.metrics.{MetricsOps, TerminationType}
import org.http4s.{Method, Status}

import scala.language.higherKinds

class Http4sServerMetrics[F[_]: Sync](meterRegistry: MeterRegistry) extends MetricsOps[F] {

  private val F = Sync[F]

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
