package com.avast.sst.monix.catnap.micrometer

import java.util.concurrent.atomic.AtomicInteger

import cats.effect.Sync
import com.avast.sst.monix.catnap.CircuitBreakerMetrics
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State._
import io.micrometer.core.instrument.MeterRegistry

/** Implements [[com.avast.sst.monix.catnap.CircuitBreakerMetrics]] for any Micrometer [[io.micrometer.core.instrument.MeterRegistry]]. */
class MicrometerCircuitBreakerMetrics[F[_]: Sync](name: String, meterRegistry: MeterRegistry) extends CircuitBreakerMetrics[F] {

  private val F = Sync[F]

  private[this] val CircuitOpened = -1
  private[this] val CircuitHalfOpened = 0
  private[this] val CircuitClosed = 1

  private val accepted = meterRegistry.counter(s"circuit-breaker.$name.accepted")
  private val rejected = meterRegistry.counter(s"circuit-breaker.$name.rejected")
  private val circuitState = meterRegistry.gauge[AtomicInteger](s"circuit-breaker.$name.state", new AtomicInteger(CircuitClosed))

  override def increaseAccepted: F[Unit] = F.delay(accepted.increment())

  override def increaseRejected: F[Unit] = F.delay(rejected.increment())

  override def setState(state: State): F[Unit] = {
    state match {
      case Closed   => F.delay(circuitState.set(CircuitClosed))
      case Open     => F.delay(circuitState.set(CircuitOpened))
      case HalfOpen => F.delay(circuitState.set(CircuitHalfOpened))
    }
  }

}
