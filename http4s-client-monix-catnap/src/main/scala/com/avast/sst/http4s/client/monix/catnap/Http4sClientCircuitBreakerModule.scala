package com.avast.sst.http4s.client.monix.catnap

import cats.effect.{Clock, Resource, Sync}
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State.{Closed, HalfOpen, Open}
import com.avast.sst.monix.catnap.{CircuitBreakerConfig, CircuitBreakerMetrics, CircuitBreakerModule}
import org.http4s.Response
import org.http4s.client.Client
import org.slf4j.LoggerFactory

object Http4sClientCircuitBreakerModule {

  private lazy val logger = LoggerFactory.getLogger("Http4sClientCircuitBreakerModule")

  /**  */
  def make[F[_]: Sync](name: String,
                       circuitBreakerConfig: CircuitBreakerConfig,
                       client: Client[F],
                       circuitBreakerMetrics: CircuitBreakerMetrics[F],
                       clock: Clock[F]): Resource[F, Client[F]] = {
    val F = Sync[F]

    class ServerFailure(val response: Response[F], val close: F[Unit]) extends Exception

    val onRejected = F.delay(logger.trace(s"Circuit breaker for $name rejected request.")) >> circuitBreakerMetrics.increaseRejected
    val onClosed = F.delay(logger.trace(s"Circuit breaker for $name closed.")) >> circuitBreakerMetrics.setState(Closed)
    val onHalfOpen = F.delay(logger.trace(s"Circuit breaker for $name half-opened.")) >> circuitBreakerMetrics.setState(HalfOpen)
    val onOpen = F.delay(logger.trace(s"Circuit breaker for $name opened.")) >> circuitBreakerMetrics.setState(Open)

    Resource.liftF(CircuitBreakerModule[F].make(circuitBreakerConfig, clock, onRejected, onClosed, onHalfOpen, onOpen)).map { cb =>
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
