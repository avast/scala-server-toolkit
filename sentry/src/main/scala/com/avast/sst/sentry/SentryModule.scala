package com.avast.sst.sentry

import cats.effect.{Resource, Sync}
import io.sentry.{SentryClient, SentryClientFactory}

import scala.jdk.CollectionConverters._
import scala.reflect.ClassTag

object SentryModule {

  /** Makes [[io.sentry.SentryClient]] initialized with the given config. */
  def make[F[_]: Sync](config: SentryConfig): Resource[F, SentryClient] = {
    Resource.make {
      Sync[F].delay {
        val dsnCustomizations = s"${config.stacktraceAppPackages.mkString("stacktrace.app.packages=", ",", "")}"
        val finalDsn = if (dsnCustomizations.nonEmpty) s"${config.dsn}?$dsnCustomizations" else config.dsn
        val sentryClient = SentryClientFactory.sentryClient(finalDsn)
        config.release.foreach(sentryClient.setRelease)
        config.environment.foreach(sentryClient.setEnvironment)
        config.distribution.foreach(sentryClient.setDist)
        config.serverName.foreach(sentryClient.setServerName)
        sentryClient.setTags(config.tags.asJava)
        sentryClient
      }
    }(sentry => Sync[F].delay(sentry.closeConnection()))
  }

  /** Makes [[io.sentry.SentryClient]] initialized with the given config and overrides the `release` property
    * with `Implementation-Version` from the `MANIFEST.MF` file inside the same JAR (package) as the `Main` class.
    */
  def makeWithReleaseFromPackage[F[_]: Sync, Main: ClassTag](config: SentryConfig): Resource[F, SentryClient] = {
    for {
      customizedConfig <- Resource.liftF {
        Sync[F].delay {
          for {
            pkg <- Option(implicitly[ClassTag[Main]].runtimeClass.getPackage)
            v <- Option(pkg.getImplementationVersion)
          } yield config.copy(release = Some(v))
        }
      }
      sentryClient <- make[F](customizedConfig.getOrElse(config))
    } yield sentryClient
  }

}
