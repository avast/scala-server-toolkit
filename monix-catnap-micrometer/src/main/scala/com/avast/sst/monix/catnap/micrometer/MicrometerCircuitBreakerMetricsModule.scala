package com.avast.sst.monix.catnap.micrometer

import cats.effect.Sync
import com.avast.sst.monix.catnap.micrometer.MicrometerCircuitBreakerMetrics.Impl
import io.micrometer.core.instrument.MeterRegistry

object MicrometerCircuitBreakerMetricsModule {

  def make[F[_]: Sync](name: String, meterRegistry: MeterRegistry): F[MicrometerCircuitBreakerMetrics[F]] = {
    Sync[F].delay(new Impl(name, meterRegistry))
  }

}
