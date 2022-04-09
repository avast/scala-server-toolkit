package com.avast.sst.http4s.server.middleware

import cats.data.{Kleisli, NonEmptyList, OptionT}
import cats.effect.Sync
import cats.syntax.functor.*
import com.avast.sst.http4s.server.middleware.CorrelationIdMiddleware.CorrelationId
import org.http4s.{Header, HttpRoutes, Request, Response}
import org.slf4j.LoggerFactory
import org.typelevel.ci.CIString
import org.typelevel.vault.Key

import java.util.UUID

/** Provides correlation ID functionality. Either generates new correlation ID for a request or takes the one sent in HTTP header and puts
  * it to [[org.http4s.Request]] attributes. It is also filled into HTTP response header.
  *
  * Use method `retrieveCorrelationId` to get the value from request attributes.
  */
class CorrelationIdMiddleware[F[_]: Sync](
    correlationIdHeaderName: CIString,
    attributeKey: Key[CorrelationId],
    generator: () => String
) {

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val F = Sync[F]

  def wrap(routes: HttpRoutes[F]): HttpRoutes[F] =
    Kleisli[OptionT[F, *], Request[F], Response[F]] { request =>
      request.headers.get(correlationIdHeaderName) match {
        case Some(NonEmptyList(header, _)) =>
          val requestWithAttribute = request.withAttribute(attributeKey, CorrelationId(header.value))
          routes(requestWithAttribute).map(r => r.withHeaders(r.headers.put(header)))
        case None =>
          for {
            newCorrelationId <- OptionT.liftF(F.delay(generator()))
            _ <- log(newCorrelationId)
            requestWithAttribute = request.withAttribute(attributeKey, CorrelationId(newCorrelationId))
            response <- routes(requestWithAttribute)
          } yield response.withHeaders(response.headers.put(Header.Raw(correlationIdHeaderName, newCorrelationId)))
      }
    }

  def retrieveCorrelationId(request: Request[F]): Option[CorrelationId] = request.attributes.lookup(attributeKey)

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

  final case class CorrelationId(value: String) extends AnyVal

  @SuppressWarnings(Array("scalafix:Disable.toString"))
  def default[F[_]: Sync]: F[CorrelationIdMiddleware[F]] = {
    Key.newKey[F, CorrelationId].map { attributeKey =>
      new CorrelationIdMiddleware(CIString("Correlation-ID"), attributeKey, () => UUID.randomUUID().toString)
    }
  }

}
