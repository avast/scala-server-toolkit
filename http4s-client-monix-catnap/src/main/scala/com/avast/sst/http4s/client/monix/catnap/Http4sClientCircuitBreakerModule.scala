package com.avast.sst.http4s.client.monix.catnap

import cats.effect.{Clock, Resource, Sync}
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State.{Closed, HalfOpen, Open}
import com.avast.sst.monix.catnap.{CircuitBreakerConfig, CircuitBreakerMetrics, CircuitBreakerModule}
import monix.catnap.CircuitBreaker
import org.http4s.Response
import org.http4s.client.Client

object Http4sClientCircuitBreakerModule {

  /** Wraps [[org.http4s.client.Client]] with [[monix.catnap.CircuitBreaker]] which provides important metrics and logs breaker events.
    *
    * The circuit breaker is special in that it also catches any HTTP responses considered as failures
    * according to the [[com.avast.sst.http4s.client.monix.catnap.HttpStatusClassifier]].
    */
  def make[F[_]: Sync](name: String,
                       client: Client[F],
                       circuitBreakerConfig: CircuitBreakerConfig,
                       circuitBreakerMetrics: CircuitBreakerMetrics[F],
                       clock: Clock[F],
                       httpStatusClassifier: HttpStatusClassifier = HttpStatusClassifier.default): F[Client[F]] = {
    CircuitBreakerModule[F]
      .makeLoggingCircuitBreaker(name, circuitBreakerConfig, clock)
      .map(makeFromCircuitBreaker(client, _, circuitBreakerMetrics, httpStatusClassifier))
  }

  /** Wraps [[org.http4s.client.Client]] with the given [[monix.catnap.CircuitBreaker]] and enriches it with important metrics.
    *
    * The circuit breaker is special in that it also catches any HTTP responses considered as failures
    * according to the [[com.avast.sst.http4s.client.monix.catnap.HttpStatusClassifier]].
    */
  def makeFromCircuitBreaker[F[_]: Sync](client: Client[F],
                                         circuitBreaker: CircuitBreaker[F],
                                         circuitBreakerMetrics: CircuitBreakerMetrics[F],
                                         httpStatusClassifier: HttpStatusClassifier = HttpStatusClassifier.default): Client[F] = {
    val F = Sync[F]

    class ServerFailure(val response: Response[F], val close: F[Unit]) extends Exception

    val monitoredCB = circuitBreaker
      .doOnRejectedTask(circuitBreakerMetrics.increaseRejected)
      .doOnClosed(circuitBreakerMetrics.setState(Closed))
      .doOnHalfOpen(circuitBreakerMetrics.setState(HalfOpen))
      .doOnOpen(circuitBreakerMetrics.setState(Open))

    Client[F] { request =>
      val raisedInternal = client.run(request).allocated.flatMap {
        case tuple @ (response, _) if !httpStatusClassifier.isFailure(response.status) => F.pure(tuple)
        case (response, close)                                                         => F.raiseError[(Response[F], F[Unit])](new ServerFailure(response, close))
      }
      val lifted = monitoredCB.protect(circuitBreakerMetrics.increaseAccepted >> raisedInternal).recover {
        case serverFailure: ServerFailure => (serverFailure.response, serverFailure.close)
      }
      Resource(lifted)
    }
  }

}
