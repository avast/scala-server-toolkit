package com.avast.sst.monix.catnap

import scala.concurrent.duration.{Duration, FiniteDuration}

final case class CircuitBreakerConfig(maxFailures: Int,
                                      resetTimeout: FiniteDuration,
                                      exponentialBackoffFactor: Double = 1.0,
                                      maxResetTimeout: Duration = Duration.Inf)
