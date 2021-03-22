package com.avast.sst.sentry

import cats.effect.{Resource, Sync}
import io.sentry.{Sentry, SentryOptions}

import scala.reflect.ClassTag

object SentryModule {

  /** Makes [[io.sentry.SentryClient]] initialized with the given config. */
  def make[F[_]: Sync](config: SentryConfig): Resource[F, Unit] = {
    Resource.make {
      Sync[F].delay {
        Sentry.init((options: SentryOptions) => {
          options.setDsn(config.dsn)
          config.release.foreach(options.setRelease)
          config.environment.foreach(options.setEnvironment)
          config.distribution.foreach(options.setDist)
          config.serverName.foreach(options.setServerName)
          config.inAppInclude.foreach(options.addInAppInclude)
        })
      }
    }(_ => Sync[F].delay(Sentry.close()))
  }

  /** Makes [[io.sentry.SentryClient]] initialized with the given config and overrides the `release` property
    * with `Implementation-Title`@`Implementation-Version` from the `MANIFEST.MF` file inside the same JAR (package) as the `Main` class.
    *
    * This format is recommended by Sentry ([[https://docs.sentry.io/workflow/releases]])
    * because releases are global and must be differentiated.
    */
  def makeWithReleaseFromPackage[F[_]: Sync, Main: ClassTag](config: SentryConfig): Resource[F, Unit] = {
    for {
      customizedConfig <- Resource.eval {
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
