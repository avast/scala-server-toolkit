package com.avast.sst.monix.catnap

import cats.effect.{Clock, Sync}
import monix.catnap.CircuitBreaker

object CircuitBreakerModule {

  /** Makes [[monix.catnap.CircuitBreaker]] initialized with the given config and optionally [[cats.effect.Clock]]. */
  def make[F[_]: Sync](config: CircuitBreakerConfig, clock: Clock[F]): F[CircuitBreaker[F]] = {
    CircuitBreaker.of[F](config.maxFailures, config.resetTimeout, config.exponentialBackoffFactor, config.maxResetTimeout)(Sync[F], clock)
  }

}
