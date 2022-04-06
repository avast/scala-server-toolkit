package com.avast.sst.sentry.pureconfig

import com.avast.sst.sentry.SentryConfig
import pureconfig.ConfigReader

trait ConfigReaders {

  implicit val sentryConfigReader: ConfigReader[SentryConfig] = implicitly[ConfigReader[SentryConfig]]

}
