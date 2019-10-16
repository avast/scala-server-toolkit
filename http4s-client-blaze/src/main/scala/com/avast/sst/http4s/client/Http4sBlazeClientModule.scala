package com.avast.sst.http4s.client

import cats.Traverse
import cats.effect.{ConcurrentEffect, Resource, Sync}
import cats.implicits._
import com.avast.sst.jvm.ssl.{SslContextConfig, SslContextModule}
import javax.net.ssl.SSLContext
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

object Http4sBlazeClientModule {

  /** Makes [[org.http4s.client.Client]] (Blaze) initialized with the given config.
    *
    * @param executionContext callback handling [[scala.concurrent.ExecutionContext]]
    */
  def make[F[_]: ConcurrentEffect](config: Http4sBlazeClientConfig, executionContext: ExecutionContext): Resource[F, Client[F]] = {
    for {
      maybeSslContext <- Resource.liftF(sslContext(config.sslContext))
      client <- {
        val builder = BlazeClientBuilder[F](executionContext)
          .withResponseHeaderTimeout(config.responseHeaderTimeout)
          .withIdleTimeout(config.idleTimeout)
          .withRequestTimeout(config.requestTimeout)
          .withConnectTimeout(config.connectTimeout)
          .withUserAgent(config.userAgent)
          .withMaxTotalConnections(config.maxTotalConnections)
          .withMaxWaitQueueLimit(config.maxWaitQueueLimit)
          .withMaxConnectionsPerRequestKey(Function.const(config.maxConnectionsPerRequestkey))
          .withCheckEndpointAuthentication(config.checkEndpointIdentification)
          .withMaxResponseLineSize(config.maxResponseLineSize)
          .withMaxHeaderLength(config.maxHeaderLength)
          .withMaxChunkSize(config.maxChunkSize)
          .withChunkBufferMaxSize(config.chunkBufferMaxSize)
          .withParserMode(config.parserMode)
          .withBufferSize(config.bufferSize)

        maybeSslContext.map(builder.withSslContext).getOrElse(builder).resource
      }
    } yield client
  }

  private def sslContext[F[_]: Sync](maybeSslContextConfig: Option[SslContextConfig]): F[Option[SSLContext]] = {
    Traverse[Option].traverse(maybeSslContextConfig)(SslContextModule.make[F])
  }

}
