package com.avast.sst.ssl

import cats.effect.Sync
import com.typesafe.config.Config
import com.typesafe.sslconfig.ssl.{
  ConfigSSLContextBuilder,
  DefaultKeyManagerFactoryWrapper,
  DefaultTrustManagerFactoryWrapper,
  SSLConfigFactory
}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import scala.language.higherKinds

object SslContextModule {

  /** Initializes [[javax.net.ssl.SSLContext]] from the provided config. */
  def make[F[_]: Sync](config: Config): F[SSLContext] = Sync[F].delay {
    val loggerFactory = Slf4jLogger.factory
    new ConfigSSLContextBuilder(loggerFactory,
                                SSLConfigFactory.parse(config, loggerFactory),
                                new DefaultKeyManagerFactoryWrapper(KeyManagerFactory.getDefaultAlgorithm),
                                new DefaultTrustManagerFactoryWrapper(TrustManagerFactory.getDefaultAlgorithm)).build
  }

}
