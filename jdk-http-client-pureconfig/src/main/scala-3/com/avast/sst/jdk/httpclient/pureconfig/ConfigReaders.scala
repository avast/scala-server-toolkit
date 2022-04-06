package com.avast.sst.jdk.httpclient.pureconfig

import pureconfig.ConfigReader
import com.avast.sst.jdk.httpclient.JdkHttpClientConfig
import pureconfig.generic.derivation.default._

trait ConfigReaders {

  implicit val jdkHttpClientConfigReader: ConfigReader[JdkHttpClientConfig] = ConfigReader.derived

}
