package com.avast.sst.ssl

import cats.effect.Sync
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.sslconfig.ssl.{
  ConfigSSLContextBuilder,
  DefaultKeyManagerFactoryWrapper,
  DefaultTrustManagerFactoryWrapper,
  SSLConfigFactory
}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import scala.language.higherKinds

object SslContextModule {

  private[this] val referenceConfig = ConfigFactory.defaultReference().getConfig("ssl-config")

  /** Initializes [[javax.net.ssl.SSLContext]] from the provided config. */
  def make[F[_]: Sync](config: Config, withReference: Boolean = true): F[SSLContext] = Sync[F].delay {
    val loggerFactory = Slf4jLogger.factory
    val finalConfig = if (withReference) config.withFallback(referenceConfig) else config
    new ConfigSSLContextBuilder(loggerFactory,
                                SSLConfigFactory.parse(finalConfig, loggerFactory),
                                new DefaultKeyManagerFactoryWrapper(KeyManagerFactory.getDefaultAlgorithm),
                                new DefaultTrustManagerFactoryWrapper(TrustManagerFactory.getDefaultAlgorithm)).build
  }

}
