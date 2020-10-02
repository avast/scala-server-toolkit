package com.avast.sst.sentry

import cats.effect.{Resource, Sync}
import io.sentry.{SentryClient, SentryOptions}

import scala.reflect.ClassTag

object SentryModule {

  /** Makes [[io.sentry.SentryClient]] initialized with the given config. */
  def make[F[_]: Sync](config: SentryConfig): Resource[F, SentryClient] = {
    Resource.make {
      Sync[F].delay {
        val dsnCustomizations = s"${config.stacktraceAppPackages.mkString("stacktrace.app.packages=", ",", "")}"
        val finalDsn = if (dsnCustomizations.nonEmpty) s"${config.dsn}?$dsnCustomizations" else config.dsn
        val options = new SentryOptions()
        options.setDsn(finalDsn)
        config.release.foreach(options.setRelease)
        config.environment.foreach(options.setEnvironment)
        config.distribution.foreach(options.setDist)
        config.serverName.foreach(options.setServerName)

        new SentryClient(options, null)
      }
    }(sentry => Sync[F].delay(sentry.close()))
  }

  /** Makes [[io.sentry.SentryClient]] initialized with the given config and overrides the `release` property
    * with `Implementation-Title`@`Implementation-Version` from the `MANIFEST.MF` file inside the same JAR (package) as the `Main` class.
    *
    * This format is recommended by Sentry ([[https://docs.sentry.io/workflow/releases]])
    * because releases are global and must be differentiated.
    */
  def makeWithReleaseFromPackage[F[_]: Sync, Main: ClassTag](config: SentryConfig): Resource[F, SentryClient] = {
    for {
      customizedConfig <- Resource.liftF {
        Sync[F].delay {
          for {
            pkg <- Option(implicitly[ClassTag[Main]].runtimeClass.getPackage)
            title <- Option(pkg.getImplementationTitle)
            version <- Option(pkg.getImplementationVersion)
          } yield config.copy(release = Some(s"$title@$version"))
        }
      }
      sentryClient <- make[F](customizedConfig.getOrElse(config))
    } yield sentryClient
  }

}
