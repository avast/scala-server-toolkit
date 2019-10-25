package com.avast.sst.http4s.client.monix.catnap.micrometer

import cats.effect.{Clock, Resource, Sync}
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import com.avast.sst.monix.catnap.CircuitBreakerMetrics.State.{Closed, HalfOpen, Open}
import com.avast.sst.monix.catnap.micrometer.MicrometerCircuitBreakerMetrics
import com.avast.sst.monix.catnap.{CircuitBreakerConfig, CircuitBreakerModule}
import io.micrometer.core.instrument.MeterRegistry
import org.http4s.Response
import org.http4s.client.Client
import org.slf4j.LoggerFactory

object Http4sClientCircuitBreakerModule {

  private lazy val logger = LoggerFactory.getLogger("Http4sClientCircuitBreakerModule")

  /**  */
  def make[F[_]: Sync](name: String,
                       circuitBreakerConfig: CircuitBreakerConfig,
                       client: Client[F],
                       meterRegistry: MeterRegistry,
                       clock: Clock[F]): Resource[F, Client[F]] = {
    val F = Sync[F]

    class ServerFailure(val response: Response[F], val close: F[Unit]) extends Exception

    for {
      metrics <- Resource.liftF(F.delay(new MicrometerCircuitBreakerMetrics[F](name, meterRegistry)))
      onRejected = F.delay(logger.trace(s"Circuit breaker for $name rejected request.")) >> metrics.increaseRejected
      onClosed = F.delay(logger.trace(s"Circuit breaker for $name closed.")) >> metrics.setState(Closed)
      onHalfOpen = F.delay(logger.trace(s"Circuit breaker for $name half-opened.")) >> metrics.setState(HalfOpen)
      onOpen = F.delay(logger.trace(s"Circuit breaker for $name opened.")) >> metrics.setState(Open)

      cb <- Resource.liftF(CircuitBreakerModule[F].make(circuitBreakerConfig, clock, onRejected, onClosed, onHalfOpen, onOpen))
    } yield Client[F] { request =>
      val raisedInternal = client.run(request).allocated.flatMap {
        case tuple @ (response, _) if response.status.code < 500 => F.pure(tuple)
        case (response, close)                                   => F.raiseError[(Response[F], F[Unit])](new ServerFailure(response, close))
      }
      val lifted = cb.protect(metrics.increaseAccepted >> raisedInternal).recover {
        case serverFailure: ServerFailure => (serverFailure.response, serverFailure.close)
      }
      Resource(lifted)
    }
  }

}
