package com.avast.sst.sentry.pureconfig

import com.avast.sst.sentry.SentryConfig
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default._
import pureconfig.generic.derivation.default._

trait ConfigReaders {

  implicit val sentryConfigReader: ConfigReader[SentryConfig] = ConfigReader.derived

}
