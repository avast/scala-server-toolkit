package com.avast.sst.jdk.httpclient.pureconfig

import com.avast.sst.jdk.httpclient.JdkHttpClientConfig
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val jdkHttpClientConfigReader: ConfigReader[JdkHttpClientConfig] = deriveReader

}
