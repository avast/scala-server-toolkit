package com.avast.sst.http4s.client.monix.catnap

import cats.effect.{Clock, Resource, Sync}
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State.{Closed, HalfOpen, Open}
import com.avast.sst.monix.catnap.{CircuitBreakerConfig, CircuitBreakerMetrics, CircuitBreakerModule}
import org.http4s.Response
import org.http4s.client.Client

object Http4sClientCircuitBreakerModule {

  /** Wraps [[org.http4s.client.Client]] with [[monix.catnap.CircuitBreaker]] which provides important metrics and logs breaker events.
    *
    * The circuit breaker is special in that it also catches any HTTP 5xx responses and considers them as failures for the circuit breaker.
    */
  def make[F[_]: Sync](name: String,
                       client: Client[F],
                       circuitBreakerConfig: CircuitBreakerConfig,
                       circuitBreakerMetrics: CircuitBreakerMetrics[F],
                       clock: Clock[F]): Resource[F, Client[F]] = {
    val F = Sync[F]

    class ServerFailure(val response: Response[F], val close: F[Unit]) extends Exception

    val cbResource = Resource.liftF(
      CircuitBreakerModule[F].makeLoggingCircuitBreaker(
        name,
        circuitBreakerConfig,
        clock,
        circuitBreakerMetrics.increaseRejected,
        circuitBreakerMetrics.setState(Closed),
        circuitBreakerMetrics.setState(HalfOpen),
        circuitBreakerMetrics.setState(Open)
      )
    )

    cbResource.map { cb =>
      Client[F] { request =>
        val raisedInternal = client.run(request).allocated.flatMap {
          case tuple @ (response, _) if response.status.code < 500 => F.pure(tuple)
          case (response, close)                                   => F.raiseError[(Response[F], F[Unit])](new ServerFailure(response, close))
        }
        val lifted = cb.protect(circuitBreakerMetrics.increaseAccepted >> raisedInternal).recover {
          case serverFailure: ServerFailure => (serverFailure.response, serverFailure.close)
        }
        Resource(lifted)
      }
    }
  }

}
