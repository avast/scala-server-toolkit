package com.avast.sst.lettuce

import com.avast.sst.lettuce.LettuceConfig.{SocketOptions, SslOptions, TimeoutOptions}
import io.lettuce.core.ClientOptions.DisconnectedBehavior
import io.lettuce.core.protocol.ProtocolVersion
import io.lettuce.core.{ClientOptions, SocketOptions => LettuceSocketOptions, TimeoutOptions => LettuceTimeoutOptions}

import java.nio.charset.Charset
import scala.concurrent.duration.Duration

final case class LettuceConfig(
    uri: String,
    pingBeforeActivateConnection: Boolean = ClientOptions.DEFAULT_PING_BEFORE_ACTIVATE_CONNECTION,
    autoReconnect: Boolean = ClientOptions.DEFAULT_AUTO_RECONNECT,
    cancelCommandsOnReconnectFailure: Boolean = ClientOptions.DEFAULT_CANCEL_CMD_RECONNECT_FAIL,
    suspendReconnectOnProtocolFailure: Boolean = ClientOptions.DEFAULT_SUSPEND_RECONNECT_PROTO_FAIL,
    requestQueueSize: Int = ClientOptions.DEFAULT_REQUEST_QUEUE_SIZE,
    disconnectedBehavior: DisconnectedBehavior = DisconnectedBehavior.DEFAULT,
    protocolVersion: Option[ProtocolVersion] = None,
    scriptCharset: Charset = ClientOptions.DEFAULT_SCRIPT_CHARSET,
    publishOnScheduler: Boolean = ClientOptions.DEFAULT_SUSPEND_RECONNECT_PROTO_FAIL,
    socketOptions: SocketOptions = SocketOptions(),
    sslOptions: SslOptions = SslOptions(),
    timeoutOptions: TimeoutOptions = TimeoutOptions()
)

object LettuceConfig {

  final case class SocketOptions(
      connectTimeout: Duration = Duration.fromNanos(LettuceSocketOptions.DEFAULT_CONNECT_TIMEOUT_DURATION.toNanos),
      keepAlive: Boolean = LettuceSocketOptions.DEFAULT_SO_KEEPALIVE,
      tcpNoDelay: Boolean = LettuceSocketOptions.DEFAULT_SO_NO_DELAY
  )

  final case class SslOptions(
      keyStoreType: Option[String] = None,
      keyStorePath: Option[String] = None,
      keyStorePassword: Option[String] = None,
      trustStorePath: Option[String] = None,
      trustStorePassword: Option[String] = None
  )

  final case class TimeoutOptions(timeoutCommands: Boolean = LettuceTimeoutOptions.DEFAULT_TIMEOUT_COMMANDS)

}
