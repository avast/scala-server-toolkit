package com.avast.sst.http4s.server

import cats.effect.{ConcurrentEffect, Resource, Timer}
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Server

import java.net.StandardSocketOptions.{SO_KEEPALIVE, SO_REUSEADDR, SO_REUSEPORT}
import java.net.{InetSocketAddress, SocketOption, StandardSocketOptions}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

object Http4sBlazeServerModule {

  /** Makes [[org.http4s.server.Server]] (Blaze) initialized with the given config and [[org.http4s.HttpApp]].
    *
    * @param executionContext
    *   callback handling [[scala.concurrent.ExecutionContext]]
    */
  def make[F[_]: ConcurrentEffect: Timer](
      config: Http4sBlazeServerConfig,
      httpApp: HttpApp[F],
      executionContext: ExecutionContext
  ): Resource[F, Server] = {
    for {
      inetSocketAddress <- Resource.eval(
        ConcurrentEffect[F].delay(
          InetSocketAddress.createUnresolved(config.listenAddress, config.listenPort)
        )
      )
      server <- {
        val builder = BlazeServerBuilder[F](executionContext)
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

        val optionalOptions = {
          import config.socketOptions._

          def set(builder: BlazeServerBuilder[F], value: Option[Boolean], option: SocketOption[java.lang.Boolean]): BlazeServerBuilder[F] =
            value.map(builder.withChannelOption[java.lang.Boolean](option, _)).getOrElse(builder)

          List[BlazeServerBuilder[F] => BlazeServerBuilder[F]](
            b => set(b, soKeepAlive, SO_KEEPALIVE),
            b => set(b, soReuseAddr, SO_REUSEADDR),
            b => set(b, soReusePort, SO_REUSEPORT)
          )
        }

        val updatedBuilder = optionalOptions.foldLeft(builder) { (b, configure) => configure(b) }
        updatedBuilder.resource
      }
    } yield server
  }
}
