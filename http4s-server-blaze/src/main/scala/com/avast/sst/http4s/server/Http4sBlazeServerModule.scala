package com.avast.sst.http4s.server

import cats.effect.{ConcurrentEffect, Resource}
import org.http4s.HttpApp
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

import java.net.{InetSocketAddress, StandardSocketOptions}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import cats.effect.Temporal

object Http4sBlazeServerModule {

  /** Makes [[org.http4s.server.Server]] (Blaze) initialized with the given config and [[org.http4s.HttpApp]].
    *
    * @param executionContext callback handling [[scala.concurrent.ExecutionContext]]
    */
  def make[F[_]: ConcurrentEffect: Temporal](
      config: Http4sBlazeServerConfig,
      httpApp: HttpApp[F],
      executionContext: ExecutionContext
  ): Resource[F, Server[F]] = {
    for {
      inetSocketAddress <- Resource.eval(
        ConcurrentEffect[F].delay(
          InetSocketAddress.createUnresolved(config.listenAddress, config.listenPort)
        )
      )
      server <-
        BlazeServerBuilder[F](executionContext)
          .bindSocketAddress(inetSocketAddress)
          .withHttpApp(httpApp)
          .withoutBanner
          .withWebSockets(config.webSocketsEnabled)
          .enableHttp2(config.http2Enabled)
          .withResponseHeaderTimeout(Duration.fromNanos(config.responseHeaderTimeout.toNanos))
          .withIdleTimeout(Duration.fromNanos(config.idleTimeout.toNanos))
          .withBufferSize(config.bufferSize)
          .withMaxRequestLineLength(config.maxRequestLineLength)
          .withMaxHeadersLength(config.maxHeadersLength)
          .withChunkBufferMaxSize(config.chunkBufferMaxSize)
          .withConnectorPoolSize(config.connectorPoolSize)
          .withMaxConnections(config.maxConnections)
          .withChannelOption[java.lang.Boolean](StandardSocketOptions.TCP_NODELAY, config.socketOptions.tcpNoDelay)
          .withChannelOption[java.lang.Boolean](StandardSocketOptions.SO_KEEPALIVE, config.socketOptions.soKeepAlive)
          .withChannelOption[java.lang.Boolean](StandardSocketOptions.SO_REUSEADDR, config.socketOptions.soReuseAddr)
          .withChannelOption[java.lang.Boolean](StandardSocketOptions.SO_REUSEPORT, config.socketOptions.soReusePort)
          .resource
    } yield server
  }
}
