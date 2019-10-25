package com.avast.sst.monix.catnap

import cats.Applicative
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State

/** Implement this trait for your monitoring system if you want to get insight into your circuit breaker. */
trait CircuitBreakerMetrics[F[_]] {

  def increaseAccepted: F[Unit]

  def increaseRejected: F[Unit]

  def setState(state: State): F[Unit]

}

object CircuitBreakerMetrics {

  sealed trait State

  object State {

    case object Closed extends State
    case object HalfOpen extends State
    case object Open extends State

  }

  def noop[F[_]](implicit F: Applicative[F]): CircuitBreakerMetrics[F] = new CircuitBreakerMetrics[F] {
    override def increaseAccepted: F[Unit] = F.unit
    override def increaseRejected: F[Unit] = F.unit
    override def setState(state: State): F[Unit] = F.unit
  }

}
