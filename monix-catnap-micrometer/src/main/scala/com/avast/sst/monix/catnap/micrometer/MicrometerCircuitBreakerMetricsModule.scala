package com.avast.sst.monix.catnap.micrometer

import cats.effect.Sync
import cats.syntax.functor._
import com.avast.sst.monix.catnap.CircuitBreakerMetrics
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State.{Closed, HalfOpen, Open}
import io.micrometer.core.instrument.MeterRegistry

import java.util.concurrent.atomic.AtomicInteger

object MicrometerCircuitBreakerMetricsModule {

  /** Makes `CircuitBreakerMetrics` from [[io.micrometer.core.instrument.MeterRegistry]]. */
  def make[F[_]: Sync](name: String, meterRegistry: MeterRegistry): F[CircuitBreakerMetrics[F]] = {
    for {
      circuitBreakerState <- Sync[F].delay(new AtomicInteger(CircuitClosed))
    } yield new MicrometerCircuitBreakerMetrics(name, meterRegistry, circuitBreakerState)
  }

  private class MicrometerCircuitBreakerMetrics[F[_]: Sync](name: String, meterRegistry: MeterRegistry, state: AtomicInteger)
      extends CircuitBreakerMetrics[F] {

    private val F = Sync[F]

    private val rejected = meterRegistry.counter(s"circuit-breaker.$name.rejected")
    private val circuitState = meterRegistry.gauge[AtomicInteger](s"circuit-breaker.$name.state", state)

    override def increaseRejected: F[Unit] = F.delay(rejected.increment())

    override def setState(state: State): F[Unit] = {
      state match {
        case Closed   => F.delay(circuitState.set(CircuitClosed))
        case Open     => F.delay(circuitState.set(CircuitOpened))
        case HalfOpen => F.delay(circuitState.set(CircuitHalfOpened))
      }
    }

  }

  private val CircuitOpened = -1
  private val CircuitHalfOpened = 0
  private val CircuitClosed = 1

}
