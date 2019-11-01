package com.avast.sst.monix.catnap.micrometer

import cats.effect.Sync
import com.avast.sst.monix.catnap.CircuitBreakerMetrics
import com.avast.sst.monix.catnap.micrometer.MicrometerCircuitBreakerMetrics.Impl
import io.micrometer.core.instrument.MeterRegistry

object MicrometerCircuitBreakerMetricsModule {

  /** Makes [[com.avast.sst.monix.catnap.CircuitBreakerMetrics]] from [[io.micrometer.core.instrument.MeterRegistry]]. */
  def make[F[_]: Sync](name: String, meterRegistry: MeterRegistry): F[CircuitBreakerMetrics[F]] = {
    Sync[F].delay(new Impl(name, meterRegistry))
  }

}
