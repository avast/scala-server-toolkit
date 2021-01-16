package com.avast.sst.ssl

import cats.effect.Sync
import cats.syntax.functor._
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.sslconfig.ssl.{
  ConfigSSLContextBuilder,
  DefaultKeyManagerFactoryWrapper,
  DefaultTrustManagerFactoryWrapper,
  SSLConfigFactory
}

import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

object SslContextModule {

  private val SslContextEnabledKey = "enabled"

  /** Initializes [[javax.net.ssl.SSLContext]] from the provided config.
    *
    * @param withReference Whether we should use reference config of "ssl-config" library as well.
    */
  def make[F[_]: Sync](config: Config, withReference: Boolean = true): F[SSLContext] =
    Sync[F].delay {
      val loggerFactory = Slf4jLogger.factory
      val finalConfig = if (withReference) config.withFallback(referenceConfigUnsafe()) else config
      new ConfigSSLContextBuilder(
        loggerFactory,
        SSLConfigFactory.parse(finalConfig, loggerFactory),
        new DefaultKeyManagerFactoryWrapper(KeyManagerFactory.getDefaultAlgorithm),
        new DefaultTrustManagerFactoryWrapper(TrustManagerFactory.getDefaultAlgorithm)
      ).build()
    }

  /** Initializes [[javax.net.ssl.SSLContext]] from the provided config if it is enabled.
    *
    * Expects a boolean value `enabled` at the root of the provided [[com.typesafe.config.Config]]
    * which determines whether to initialize the context or not.
    *
    * @param withReference Whether we should use reference config of "ssl-config" library as well.
    */
  def makeIfEnabled[F[_]: Sync](config: Config, withReference: Boolean = true): F[Option[SSLContext]] = {
    if (config.hasPath(SslContextEnabledKey) && config.getBoolean(SslContextEnabledKey)) {
      make(config, withReference).map(Some(_))
    } else {
      Sync[F].delay(None)
    }

  }

  private def referenceConfigUnsafe(): Config = ConfigFactory.defaultReference().getConfig("ssl-config")

}
