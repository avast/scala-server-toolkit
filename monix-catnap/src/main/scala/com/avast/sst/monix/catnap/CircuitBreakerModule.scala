package com.avast.sst.monix.catnap

import cats.effect.{Clock, Sync}
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State.{Closed, HalfOpen, Open}
import monix.catnap.CircuitBreaker
import org.slf4j.LoggerFactory

class CircuitBreakerModule[F[_]](implicit F: Sync[F]) {

  /** Makes [[monix.catnap.CircuitBreaker]] initialized with the given config and `cats.effect.Clock`. */
  def make(config: CircuitBreakerConfig,
           clock: Clock[F],
           onRejected: F[Unit] = F.unit,
           onClosed: F[Unit] = F.unit,
           onHalfOpen: F[Unit] = F.unit,
           onOpen: F[Unit] = F.unit): F[CircuitBreaker[F]] = {
    CircuitBreaker[F].of(config.maxFailures,
                         config.resetTimeout,
                         config.exponentialBackoffFactor,
                         config.maxResetTimeout,
                         onRejected,
                         onClosed,
                         onHalfOpen,
                         onOpen)(clock)
  }

}

object CircuitBreakerModule {

  /** Creates [[com.avast.sst.monix.catnap.CircuitBreakerModule]] specialed for `F[_]: Sync`. */
  def apply[F[_]: Sync]: CircuitBreakerModule[F] = new CircuitBreakerModule[F]

  /** Wraps [[monix.catnap.CircuitBreaker]] and makes it log important events (e.g. onClose, onOpen). */
  def withLogging[F[_]: Sync](name: String, circuitBreaker: CircuitBreaker[F]): CircuitBreaker[F] = {
    val F = Sync[F]

    lazy val logger = LoggerFactory.getLogger(s"${this.getClass}.$name")

    val loggingOnRejected = F.delay(logger.trace(s"Circuit breaker for $name rejected request."))
    val loggingOnClosed = F.delay(logger.trace(s"Circuit breaker for $name closed."))
    val loggingOnHalfOpen = F.delay(logger.trace(s"Circuit breaker for $name half-opened."))
    val loggingOnOpen = F.delay(logger.trace(s"Circuit breaker for $name opened."))

    circuitBreaker
      .doOnRejectedTask(loggingOnRejected)
      .doOnClosed(loggingOnClosed)
      .doOnHalfOpen(loggingOnHalfOpen)
      .doOnOpen(loggingOnOpen)
  }

  /** Wraps [[monix.catnap.CircuitBreaker]] and adds monitoring metrics (e.g. number of rejected tasks). */
  def withMetrics[F[_]: Sync](circuitBreakerMetrics: CircuitBreakerMetrics[F], circuitBreaker: CircuitBreaker[F]): CircuitBreaker[F] = {
    circuitBreaker
      .doOnRejectedTask(circuitBreakerMetrics.increaseRejected)
      .doOnClosed(circuitBreakerMetrics.setState(Closed))
      .doOnHalfOpen(circuitBreakerMetrics.setState(HalfOpen))
      .doOnOpen(circuitBreakerMetrics.setState(Open))
  }

}
