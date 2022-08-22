package com.avast.sst.http4s.client

import cats.effect.{Blocker, Concurrent, ContextShift, Resource, Timer}
import com.avast.sst.http4s.client.Http4sEmberClientConfig.SocketOptions
import fs2.io.tcp.SocketOptionMapping
import fs2.io.tls.TLSContext
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder

import java.net.StandardSocketOptions

object Http4sEmberClientModule {
  def make[F[_]: Concurrent: Timer: ContextShift](
      config: Http4sEmberClientConfig,
      blocker: Option[Blocker] = None,
      tlsContext: Option[TLSContext] = None
  ): Resource[F, Client[F]] = {
    val builder = EmberClientBuilder
      .default[F]
      .withMaxTotal(config.maxTotal)
      .withMaxPerKey(Function.const(config.maxPerKey))
      .withIdleTimeInPool(config.idleTimeInPool)
      .withChunkSize(config.chunkSize)
      .withMaxResponseHeaderSize(config.maxResponseHeaderSize)
      .withIdleConnectionTime(config.idleConnectionTime)
      .withTimeout(config.timeout)
      .withAdditionalSocketOptions(socketOptionMapping(config.socketOptions))
      .withUserAgent(config.userAgent)
      .withCheckEndpointAuthentication(config.checkEndpointIdentification)

    val builderWithMaybeBlocker = blocker.fold(builder)(builder.withBlocker)
    val builderWithMaybeTSL = tlsContext.fold(builderWithMaybeBlocker)(builderWithMaybeBlocker.withTLSContext)

    builderWithMaybeTSL.build
  }

  def socketOptionMapping(socketOptions: SocketOptions) =
    List(
      SocketOptionMapping[java.lang.Boolean](StandardSocketOptions.SO_REUSEADDR, socketOptions.reuseAddress),
      SocketOptionMapping[java.lang.Integer](StandardSocketOptions.SO_SNDBUF, socketOptions.sendBufferSize),
      SocketOptionMapping[java.lang.Integer](StandardSocketOptions.SO_RCVBUF, socketOptions.receiveBufferSize),
      SocketOptionMapping[java.lang.Boolean](StandardSocketOptions.SO_KEEPALIVE, socketOptions.keepAlive),
      SocketOptionMapping[java.lang.Boolean](StandardSocketOptions.TCP_NODELAY, socketOptions.noDelay)
    )
}
