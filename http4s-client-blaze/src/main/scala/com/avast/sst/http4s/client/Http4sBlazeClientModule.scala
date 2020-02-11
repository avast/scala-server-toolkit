package com.avast.sst.http4s.client

import cats.effect.{ConcurrentEffect, Resource}
import javax.net.ssl.SSLContext
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext
object Http4sBlazeClientModule {

  /** Makes [[org.http4s.client.Client]] (Blaze) initialized with the given config.
    *
    * @param executionContext callback handling [[scala.concurrent.ExecutionContext]]
    */
  def make[F[_]: ConcurrentEffect](config: Http4sBlazeClientConfig,
                                   executionContext: ExecutionContext,
                                   sslContext: Option[SSLContext] = None): Resource[F, Client[F]] = {
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

    sslContext.map(builder.withSslContext).getOrElse(builder).resource
  }

}
