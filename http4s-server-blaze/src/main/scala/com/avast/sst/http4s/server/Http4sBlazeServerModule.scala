package com.avast.sst.http4s.server

import java.net.{InetSocketAddress, StandardSocketOptions}

import cats.effect.{ConcurrentEffect, Resource, Timer}
import org.http4s.HttpApp
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

object Http4sBlazeServerModule {

  /** Makes [[org.http4s.server.Server]] (Blaze) initialized with the given config and [[org.http4s.HttpApp]].
    *
    * @param executionContext callback handling [[scala.concurrent.ExecutionContext]]
    */
  def make[F[_]: ConcurrentEffect: Timer](
      config: Http4sBlazeServerConfig,
      httpApp: HttpApp[F],
      executionContext: ExecutionContext
  ): Resource[F, Server[F]] = {
    for {
      inetSocketAddress <- Resource.liftF(
        ConcurrentEffect[F].delay(
          InetSocketAddress.createUnresolved(config.listenAddress, config.listenPort)
        )
      )
      server <- BlazeServerBuilder[F]
        .bindSocketAddress(inetSocketAddress)
        .withHttpApp(httpApp)
        .withExecutionContext(executionContext)
        .withoutBanner
        .withNio2(config.nio2Enabled)
        .withWebSockets(config.webSocketsEnabled)
        .enableHttp2(config.http2Enabled)
        .withResponseHeaderTimeout(Duration.fromNanos(config.responseHeaderTimeout.toNanos))
        .withIdleTimeout(Duration.fromNanos(config.idleTimeout.toNanos))
        .withBufferSize(config.bufferSize)
        .withMaxRequestLineLength(config.maxRequestLineLength)
        .withMaxHeadersLength(config.maxHeadersLength)
        .withChunkBufferMaxSize(config.chunkBufferMaxSize)
        .withConnectorPoolSize(config.connectorPoolSize)
        .withChannelOption[java.lang.Boolean](StandardSocketOptions.TCP_NODELAY, config.socketOptions.tcpNoDelay)
        .resource
    } yield server
  }
}
