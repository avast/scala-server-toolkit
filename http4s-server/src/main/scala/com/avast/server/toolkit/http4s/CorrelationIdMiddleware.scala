package com.avast.server.toolkit.http4s

import java.util.UUID

import cats.data.{Kleisli, OptionT}
import cats.effect.Sync
import org.http4s.util.CaseInsensitiveString
import org.http4s.{Header, HttpRoutes, Request, Response}
import org.slf4j.LoggerFactory

import scala.language.higherKinds

/** Provides correlation ID functionality. Either generates new correlation ID for a request (random UUID) or takes the one sent in
  * HTTP header [[com.avast.server.toolkit.http4s.CorrelationIdMiddleware.CorrelationIdHeaderName]] and fills it into HTTP response.
  */
object CorrelationIdMiddleware {

  private val logger = LoggerFactory.getLogger("CorrelationIdMiddleware")

  val CorrelationIdHeaderName = CaseInsensitiveString("Correlation-Id")

  def wrap[F[_]: Sync](routes: HttpRoutes[F]): HttpRoutes[F] = Kleisli[OptionT[F, *], Request[F], Response[F]] { request =>
    request.headers.get(CorrelationIdHeaderName) match {
      case Some(header) => routes(request).map(r => r.withHeaders(r.headers.put(Header(CorrelationIdHeaderName.value, header.value))))
      case None =>
        for {
          newCorrelationId <- OptionT.liftF(Sync[F].delay(UUID.randomUUID()))
          _ <- log(newCorrelationId)
          response <- routes(request.withHeaders(request.headers.put(Header(CorrelationIdHeaderName.value, newCorrelationId.toString))))
                       .map(r => r.withHeaders(r.headers.put(Header(CorrelationIdHeaderName.value, newCorrelationId.toString))))
        } yield response
    }
  }

  private def log[F[_]: Sync](newCorrelationId: UUID) = {
    OptionT.liftF {
      Sync[F].delay {
        if (logger.isDebugEnabled()) {
          logger.debug(s"Generated new correlation ID: ${newCorrelationId.toString}")
        }
      }
    }
  }
}
