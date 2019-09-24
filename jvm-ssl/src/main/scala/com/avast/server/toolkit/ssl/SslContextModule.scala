package com.avast.server.toolkit.ssl

import java.nio.file.Files
import java.security.KeyStore

import cats.effect.{Resource, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.show._
import javax.net.ssl.{KeyManager, KeyManagerFactory, SSLContext, TrustManager, TrustManagerFactory}

@SuppressWarnings(Array("org.wartremover.warts.Null"))
object SslContextModule {

  /** Loads [[javax.net.ssl.SSLContext]] and fills it with key/trust managers from the provided config. */
  def make[F[_]: Sync](config: SslContextConfig): F[SSLContext] = {
    val F = Sync[F]

    for {
      systemKeyManagers <- F.delay {
                            if (config.loadSystemKeyManagers)
                              getKeyManagers(null, null)
                            else List.empty
                          }
      systemTrustManagers <- F.delay {
                              if (config.loadSystemTrustManagers)
                                getTrustManagers(null)
                              else List.empty
                            }
      keyManagers <- config
                      .keystore
                      .map { keystore =>
                        Resource.fromAutoCloseable(F.delay(Files.newInputStream(keystore.path))).use { keystoreInputStream =>
                          val ks = KeyStore.getInstance(keystore.keystoreType.show)
                          ks.load(keystoreInputStream, keystore.password.toCharArray)
                          F.delay(getKeyManagers(ks, keystore.keyPassword.orNull) ++ systemKeyManagers)
                        }
                      }
                      .getOrElse(F.delay(List.empty))
                      .map(_.toArray)
      trustManagers <- config
                        .truststore
                        .map { truststore =>
                          Resource.fromAutoCloseable(F.delay(Files.newInputStream(truststore.path))).use { truststoreInpuStream =>
                            val ks = KeyStore.getInstance(truststore.keystoreType.show)
                            ks.load(truststoreInpuStream, truststore.password.toCharArray)
                            F.delay(getTrustManagers(ks) ++ systemTrustManagers)
                          }
                        }
                        .getOrElse(F.delay(List.empty))
                        .map(_.toArray)
    } yield {
      val context = SSLContext.getInstance(config.protocol.show)
      context.init(keyManagers, trustManagers, null)
      context
    }
  }

  private def getKeyManagers(keystore: KeyStore, keyPassword: String): List[KeyManager] = {
    val factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    factory.init(keystore, Option(keyPassword).map(_.toCharArray).orNull)

    factory
      .getKeyManagers
      .toList
  }

  private def getTrustManagers(keystore: KeyStore): List[TrustManager] = {
    val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    factory.init(keystore)

    factory
      .getTrustManagers
      .toList
  }

}
