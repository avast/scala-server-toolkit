package com.avast.server.toolkit.http4s

import java.util.concurrent.TimeUnit

import com.avast.server.toolkit.http4s.Http4sBlazeServerConfig.SocketOptions
import org.http4s.blaze.channel
import org.http4s.server.defaults

import scala.concurrent.duration.{Duration, FiniteDuration}

final case class Http4sBlazeServerConfig(
  listenAddress: String,
  listenPort: Int,
  nio2Enabled: Boolean = true,
  webSocketsEnabled: Boolean = false,
  http2Enabled: Boolean = false,
  responseHeaderTimeout: FiniteDuration = Duration(defaults.ResponseTimeout.toNanos, TimeUnit.NANOSECONDS),
  idleTimeout: FiniteDuration = Duration(defaults.IdleTimeout.toNanos, TimeUnit.NANOSECONDS),
  bufferSize: Int = 64 * 1024,
  maxRequestLineLength: Int = 4 * 1024,
  maxHeadersLength: Int = 40 * 1024,
  chunkBufferMaxSize: Int = 1024 * 1024,
  connectorPoolSize: Int = channel.DefaultPoolSize,
  socketOptions: SocketOptions = SocketOptions()
)

object Http4sBlazeServerConfig {

  final case class SocketOptions(tcpNoDelay: Boolean = true)

}
