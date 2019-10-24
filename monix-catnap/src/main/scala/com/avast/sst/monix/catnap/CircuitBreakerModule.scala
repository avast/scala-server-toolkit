package com.avast.sst.monix.catnap

import cats.effect.{Clock, Sync}
import monix.catnap.CircuitBreaker

class CircuitBreakerModule[F[_]](implicit F: Sync[F]) {

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

}

object CircuitBreakerModule {

  def apply[F[_]: Sync] = new CircuitBreakerModule[F]

}
