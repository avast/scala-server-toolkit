package com.avast.sst.sentry.pureconfig

import com.avast.sst.sentry.SentryConfig
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val sentryConfigReader: ConfigReader[SentryConfig] = deriveReader

}
