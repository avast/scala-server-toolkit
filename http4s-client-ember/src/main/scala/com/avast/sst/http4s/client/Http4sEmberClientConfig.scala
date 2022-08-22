package com.avast.sst.http4s.client

import com.avast.sst.http4s.client.Http4sEmberClientConfig.{Defaults, SocketOptions}
import org.http4s.ProductId
import org.http4s.client.defaults
import org.http4s.headers.`User-Agent`

import scala.concurrent.duration.{DurationInt, FiniteDuration}

final case class Http4sEmberClientConfig(
    maxTotal: Int = Defaults.maxTotal,
    maxPerKey: Int = Defaults.maxPerKey,
    idleTimeInPool: FiniteDuration = Defaults.idleTimeInPool,
    chunkSize: Int = Defaults.chunkSize,
    maxResponseHeaderSize: Int = Defaults.maxResponseHeaderSize,
    idleConnectionTime: FiniteDuration = Defaults.idleConnectionTime,
    timeout: FiniteDuration = Defaults.timeout,
    socketOptions: SocketOptions = SocketOptions(),
    userAgent: `User-Agent` = Defaults.userAgent,
    checkEndpointIdentification: Boolean = Defaults.checkEndpointIdentification
)

object Http4sEmberClientConfig {
  final case class SocketOptions(
      reuseAddress: Boolean = true,
      sendBufferSize: Int = 256 * 1024,
      receiveBufferSize: Int = 256 * 1024,
      keepAlive: Boolean = false,
      noDelay: Boolean = false
  )

  object Defaults {
    val maxTotal = 100
    val maxPerKey = 100
    val idleTimeInPool: FiniteDuration = 30.seconds
    val chunkSize: Int = 32 * 1024
    val maxResponseHeaderSize: Int = 4096
    val idleConnectionTime: FiniteDuration = defaults.RequestTimeout
    val timeout: FiniteDuration = defaults.RequestTimeout
    val userAgent: `User-Agent` = `User-Agent`(ProductId("http4s-ember", Some(org.http4s.BuildInfo.version)))
    val checkEndpointIdentification = true
  }
}
