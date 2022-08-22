package com.avast.sst.http4s.server

import com.avast.sst.http4s.server.Http4sEmberServerConfig.Defaults
import org.http4s.server.defaults

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.*

final case class Http4sEmberServerConfig(
    host: String = Defaults.host,
    port: Int = Defaults.port,
    maxConnections: Int = Defaults.maxConnections,
    receiveBufferSize: Int = Defaults.receiveBufferSize,
    maxHeaderSize: Int = Defaults.maxHeaderSize,
    requestHeaderReceiveTimeout: FiniteDuration = Defaults.requestHeaderReceiveTimeout,
    idleTimeout: FiniteDuration = Defaults.idleTimeout,
    shutdownTimeout: FiniteDuration = Defaults.shutdownTimeout
)

object Http4sEmberServerConfig {
  object Defaults {
    val host: String = defaults.IPv4Host
    val port: Int = 8080
    val maxConnections: Int = defaults.MaxConnections
    val receiveBufferSize: Int = 256 * 1024
    val maxHeaderSize: Int = defaults.MaxHeadersSize
    val requestHeaderReceiveTimeout: FiniteDuration = 5.seconds
    val idleTimeout: FiniteDuration = Duration(defaults.IdleTimeout.toNanos, TimeUnit.NANOSECONDS)
    val shutdownTimeout: FiniteDuration = Duration(defaults.ShutdownTimeout.toNanos, TimeUnit.NANOSECONDS)
  }
}
