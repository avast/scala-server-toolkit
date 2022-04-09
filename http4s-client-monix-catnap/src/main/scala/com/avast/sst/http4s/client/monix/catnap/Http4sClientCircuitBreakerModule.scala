package com.avast.sst.http4s.client.monix.catnap

import cats.effect.{Resource, Sync}
import cats.syntax.applicativeError.*
import cats.syntax.flatMap.*
import monix.catnap.CircuitBreaker
import org.http4s.Response
import org.http4s.client.Client

object Http4sClientCircuitBreakerModule {

  /** Wraps [[org.http4s.client.Client]] with the given [[monix.catnap.CircuitBreaker]].
    *
    * The circuit breaker is special in that it also catches any HTTP responses considered as server failures according to the
    * [[com.avast.sst.http4s.client.monix.catnap.HttpStatusClassifier]].
    */
  def make[F[_]: Sync](
      client: Client[F],
      circuitBreaker: CircuitBreaker[F],
      httpStatusClassifier: HttpStatusClassifier = HttpStatusClassifier.default
  ): Client[F] = {
    val F = Sync[F]

    class ServerFailure(val response: Response[F], val close: F[Unit]) extends Exception

    Client[F] { request =>
      val raisedInternal = client.run(request).allocated.flatMap {
        case tuple @ (response, _) if !httpStatusClassifier.isServerFailure(response.status) => F.pure(tuple)
        case (response, close) => F.raiseError[(Response[F], F[Unit])](new ServerFailure(response, close))
      }
      val lifted = circuitBreaker.protect(raisedInternal).recover { case serverFailure: ServerFailure =>
        (serverFailure.response, serverFailure.close)
      }
      Resource(lifted)
    }
  }

}
