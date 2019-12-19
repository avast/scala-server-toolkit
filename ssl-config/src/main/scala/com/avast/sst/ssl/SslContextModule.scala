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

  /**
    * Initializes [[javax.net.ssl.SSLContext]] from the provided config.
    * @param withReference Whether we should use reference config of "ssl-config" library as well.
    */
  def make[F[_]: Sync](config: Config, withReference: Boolean = true): F[SSLContext] = Sync[F].delay {
    val loggerFactory = Slf4jLogger.factory
    val finalConfig = if (withReference) config.withFallback(referenceConfigUnsafe()) else config
    new ConfigSSLContextBuilder(loggerFactory,
                                SSLConfigFactory.parse(finalConfig, loggerFactory),
                                new DefaultKeyManagerFactoryWrapper(KeyManagerFactory.getDefaultAlgorithm),
                                new DefaultTrustManagerFactoryWrapper(TrustManagerFactory.getDefaultAlgorithm)).build
  }

  private def referenceConfigUnsafe(): Config = ConfigFactory.defaultReference().getConfig("ssl-config")

}
