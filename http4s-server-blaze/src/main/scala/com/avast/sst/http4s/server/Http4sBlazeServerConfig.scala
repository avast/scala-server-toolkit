package com.avast.sst.http4s.server

import com.avast.sst.http4s.server.Http4sBlazeServerConfig.SocketOptions
import org.http4s.blaze.channel
import org.http4s.server.defaults

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, FiniteDuration}

final case class Http4sBlazeServerConfig(
    listenAddress: String,
    listenPort: Int,
    webSocketsEnabled: Boolean = false,
    http2Enabled: Boolean = false,
    responseHeaderTimeout: FiniteDuration = Duration(defaults.ResponseTimeout.toNanos, TimeUnit.NANOSECONDS),
    idleTimeout: FiniteDuration = Duration(defaults.IdleTimeout.toNanos, TimeUnit.NANOSECONDS),
    bufferSize: Int = 64 * 1024,
    maxRequestLineLength: Int = 4 * 1024,
    maxHeadersLength: Int = defaults.MaxHeadersSize,
    chunkBufferMaxSize: Int = 1024 * 1024,
    connectorPoolSize: Int = channel.DefaultPoolSize,
    maxConnections: Int = defaults.MaxConnections,
    socketOptions: SocketOptions = SocketOptions()
)

object Http4sBlazeServerConfig {

  def localhost8080: Http4sBlazeServerConfig = Http4sBlazeServerConfig("127.0.0.1", 8080)

  final case class SocketOptions(
      tcpNoDelay: Boolean = true,
      soKeepAlive: Boolean = true,
      soReuseAddr: Boolean = true,
      soReusePort: Boolean = true
  )

}
