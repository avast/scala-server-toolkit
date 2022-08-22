package com.avast.sst.http4s.server

import cats.effect.{Blocker, Concurrent, ContextShift, Resource, Timer}
import fs2.io.tls.{TLSContext, TLSParameters}
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server

object Http4sEmberServerModule {
  def make[F[_]: Concurrent: Timer: ContextShift](
      config: Http4sEmberServerConfig,
      httpApp: HttpApp[F],
      blocker: Option[Blocker] = None,
      tls: Option[(TLSContext, TLSParameters)] = None
  ): Resource[F, Server] = {
    val builder = EmberServerBuilder
      .default[F]
      .withHost(config.host)
      .withPort(config.port)
      .withHttpApp(httpApp)
      .withMaxConnections(config.maxConnections)
      .withReceiveBufferSize(config.receiveBufferSize)
      .withMaxHeaderSize(config.maxHeaderSize)
      .withRequestHeaderReceiveTimeout(config.requestHeaderReceiveTimeout)
      .withIdleTimeout(config.idleTimeout)
      .withShutdownTimeout(config.shutdownTimeout)

    val builderWithMaybeBlocker = blocker.fold(builder)(builder.withBlocker)
    val builderWithMaybeTLS = tls.fold(builderWithMaybeBlocker)(t => builderWithMaybeBlocker.withTLS(t._1, t._2))

    builderWithMaybeTLS.build
  }
}
