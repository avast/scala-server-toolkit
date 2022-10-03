package com.avast.sst.pureconfig

import com.typesafe.config.Config
import pureconfig.ConfigReader

/** Used to retrieve both parsed configuration object and underlying [[Config]] instance. */
final case class WithConfig[T](value: T, config: Config)

object WithConfig {
  implicit def configReader[T: ConfigReader]: ConfigReader[WithConfig[T]] =
    for {
      config <- ConfigReader[Config]
      value <- ConfigReader[T]
    } yield WithConfig(value, config)

}
