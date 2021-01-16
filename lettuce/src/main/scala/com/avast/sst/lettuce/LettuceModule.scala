package com.avast.sst.lettuce

import cats.effect.{Async, Resource, Sync}
import cats.syntax.either._
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.{ClientOptions, RedisClient, RedisURI, SocketOptions, SslOptions, TimeoutOptions}

import java.io.File
import java.time.Duration

object LettuceModule {

  /** Makes [[io.lettuce.core.RedisClient]] initialized with the given config and optionally [[io.lettuce.core.resource.ClientResources]]. */
  def makeClient[F[_]: Sync](config: LettuceConfig, clientResources: Option[ClientResources] = None): Resource[F, RedisClient] = {
    lazy val create = clientResources match {
      case Some(resources) => RedisClient.create(resources)
      case None            => RedisClient.create()
    }
    val sync = Sync[F]
    Resource.make {
      sync.delay {
        val client = create
        client.setOptions(makeClientOptions(config))
        client
      }
    }(c => sync.delay(c.shutdown()))
  }

  /** Makes [[io.lettuce.core.api.StatefulRedisConnection]] initialized with the given config and optionally [[io.lettuce.core.resource.ClientResources]]. */
  def makeConnection[F[_]: Async, K, V](
      config: LettuceConfig,
      clientResources: Option[ClientResources] = None
  )(implicit codec: RedisCodec[K, V]): Resource[F, StatefulRedisConnection[K, V]] = {
    makeClient[F](config, clientResources).flatMap { client =>
      val async = Async[F]
      Resource.make[F, StatefulRedisConnection[K, V]] {
        async.asyncF[StatefulRedisConnection[K, V]] { cb =>
          async.delay {
            client
              .connectAsync(codec, RedisURI.create(config.uri))
              .handle[Unit] { (connection, ex) =>
                if (ex == null) {
                  cb(connection.asRight)
                } else {
                  cb(ex.asLeft)
                }
              }
            ()
          }
        }
      }(c => async.delay(c.close()))
    }
  }

  private def makeClientOptions(config: LettuceConfig): ClientOptions =
    ClientOptions
      .builder()
      .pingBeforeActivateConnection(config.pingBeforeActivateConnection)
      .autoReconnect(config.autoReconnect)
      .cancelCommandsOnReconnectFailure(config.cancelCommandsOnReconnectFailure)
      .suspendReconnectOnProtocolFailure(config.suspendReconnectOnProtocolFailure)
      .requestQueueSize(config.requestQueueSize)
      .disconnectedBehavior(config.disconnectedBehavior)
      .protocolVersion(config.protocolVersion.orNull)
      .scriptCharset(config.scriptCharset)
      .publishOnScheduler(config.publishOnScheduler)
      .socketOptions(
        SocketOptions
          .builder()
          .connectTimeout(Duration.ofNanos(config.socketOptions.connectTimeout.toNanos))
          .keepAlive(config.socketOptions.keepAlive)
          .tcpNoDelay(config.socketOptions.tcpNoDelay)
          .build()
      )
      .timeoutOptions(TimeoutOptions.builder().timeoutCommands(config.timeoutOptions.timeoutCommands).build())
      .sslOptions {
        val opts = SslOptions
          .builder()
          .jdkSslProvider()

        config.sslOptions.keyStoreType.foreach(opts.keyStoreType)
        config.sslOptions.keyStorePath.zip(config.sslOptions.keyStorePassword).foreach { case (path, pass) =>
          opts.keystore(new File(path), pass.toCharArray)
        }
        config.sslOptions.trustStorePath.zip(config.sslOptions.trustStorePassword).foreach { case (path, pass) =>
          opts.truststore(new File(path), pass)
        }

        opts.build()
      }
      .build()

}
