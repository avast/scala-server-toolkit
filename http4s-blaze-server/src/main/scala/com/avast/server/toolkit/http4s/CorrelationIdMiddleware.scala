package com.avast.server.toolkit.http4s

import java.util.UUID

import cats.data.{Kleisli, OptionT}
import cats.effect.Sync
import io.chrisdavenport.vault.Key
import org.http4s.util.CaseInsensitiveString
import org.http4s.{Header, HttpRoutes, Request, Response}
import org.slf4j.LoggerFactory

import scala.language.higherKinds

/** Provides correlation ID functionality. Either generates new correlation ID for a request or takes the one sent in HTTP header
  * and puts it to [[org.http4s.Request]] attributes. It is also filled into HTTP response header.
  */
class CorrelationIdMiddleware[F[_]: Sync](correlationIdHeaderName: CaseInsensitiveString, generator: () => String) {

  private val logger = LoggerFactory.getLogger("CorrelationIdMiddleware")

  private val F = Sync[F]

  private val attributeKey = Key.newKey[F, String]

  def wrap(routes: HttpRoutes[F]): HttpRoutes[F] = Kleisli[OptionT[F, *], Request[F], Response[F]] { request =>
    request.headers.get(correlationIdHeaderName) match {
      case Some(header) =>
        for {
          key <- OptionT.liftF(attributeKey)
          requestWithAttribute = request.withAttribute(key, header.value)
          response <- routes(requestWithAttribute)
        } yield response.withAttribute(key, header.value)
      case None =>
        for {
          newCorrelationId <- OptionT.liftF(F.delay(generator()))
          _ <- log(newCorrelationId)
          key <- OptionT.liftF(attributeKey)
          requestWithAttribute = request.withAttribute(key, newCorrelationId)
          response <- routes(requestWithAttribute)
        } yield response.withHeaders(response.headers.put(Header(correlationIdHeaderName.value, newCorrelationId)))
    }
  }

  private def log(newCorrelationId: String) = {
    OptionT.liftF {
      F.delay {
        if (logger.isDebugEnabled()) {
          logger.debug(s"Generated new correlation ID: $newCorrelationId")
        }
      }
    }
  }
}

object CorrelationIdMiddleware {

  def default[F[_]: Sync]: CorrelationIdMiddleware[F] = {
    new CorrelationIdMiddleware(CaseInsensitiveString("Correlation-ID"), () => UUID.randomUUID().toString)
  }

}
