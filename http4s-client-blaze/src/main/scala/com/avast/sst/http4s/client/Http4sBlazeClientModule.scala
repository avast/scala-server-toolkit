package com.avast.sst.http4s.client

import cats.effect.{ConcurrentEffect, Resource}
import com.avast.sst.http4s.client.Http4sBlazeClientConfig.SocketOptions
import org.http4s.blaze.channel.{ChannelOptions, OptionValue}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client

import java.net.StandardSocketOptions
import javax.net.ssl.SSLContext
import scala.concurrent.ExecutionContext
object Http4sBlazeClientModule {

  /** Makes [[org.http4s.client.Client]] (Blaze) initialized with the given config.
    *
    * @param executionContext
    *   callback handling [[scala.concurrent.ExecutionContext]]
    */
  def make[F[_]: ConcurrentEffect](
      config: Http4sBlazeClientConfig,
      executionContext: ExecutionContext,
      sslContext: Option[SSLContext] = None
  ): Resource[F, Client[F]] = {
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

    val builderWithMaybeSocketOptions = config.socketOptions.fold(builder)(s => builder.withChannelOptions(channelOptions(s)))
    val builderWithMaybeTLS = sslContext.fold(builderWithMaybeSocketOptions)(builderWithMaybeSocketOptions.withSslContext)

    builderWithMaybeTLS.resource
  }

  def channelOptions(socketOptions: SocketOptions): ChannelOptions =
    ChannelOptions(
      Vector(
        OptionValue[java.lang.Boolean](StandardSocketOptions.SO_REUSEADDR, socketOptions.reuseAddress),
        OptionValue[java.lang.Integer](StandardSocketOptions.SO_SNDBUF, socketOptions.sendBufferSize),
        OptionValue[java.lang.Integer](StandardSocketOptions.SO_RCVBUF, socketOptions.receiveBufferSize),
        OptionValue[java.lang.Boolean](StandardSocketOptions.SO_KEEPALIVE, socketOptions.keepAlive),
        OptionValue[java.lang.Boolean](StandardSocketOptions.TCP_NODELAY, socketOptions.noDelay)
      )
    )
}
