package com.avast.sst.monix.catnap

import cats.effect.{Clock, Sync}
import cats.syntax.flatMap._
import monix.catnap.CircuitBreaker
import org.slf4j.LoggerFactory

class CircuitBreakerModule[F[_]](implicit F: Sync[F]) {

  private lazy val logger = LoggerFactory.getLogger(this.getClass)

  /** Makes [[monix.catnap.CircuitBreaker]] initialized with the given config and [[cats.effect.Clock]]. */
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

  /** Makes [[monix.catnap.CircuitBreaker]] initialized with the given config and [[cats.effect.Clock]].
    *
    * The returned circuit breaker is enriched with logging messages for important events (e.g. onClose, onOpen).
    */
  def makeLoggingCircuitBreaker(name: String,
                                config: CircuitBreakerConfig,
                                clock: Clock[F],
                                onRejected: F[Unit] = F.unit,
                                onClosed: F[Unit] = F.unit,
                                onHalfOpen: F[Unit] = F.unit,
                                onOpen: F[Unit] = F.unit): F[CircuitBreaker[F]] = {
    val loggingOnRejected = F.delay(logger.trace(s"Circuit breaker for $name rejected request.")) >> onRejected
    val loggingOnClosed = F.delay(logger.trace(s"Circuit breaker for $name closed.")) >> onClosed
    val loggingOnHalfOpen = F.delay(logger.trace(s"Circuit breaker for $name half-opened.")) >> onHalfOpen
    val loggingOnOpen = F.delay(logger.trace(s"Circuit breaker for $name opened.")) >> onOpen

    make(config, clock, loggingOnRejected, loggingOnClosed, loggingOnHalfOpen, loggingOnOpen)
  }

}

object CircuitBreakerModule {

  /** Creates [[com.avast.sst.monix.catnap.CircuitBreakerModule]] specialed for `F[_]: Sync`. */
  def apply[F[_]: Sync]: CircuitBreakerModule[F] = new CircuitBreakerModule[F]

}
