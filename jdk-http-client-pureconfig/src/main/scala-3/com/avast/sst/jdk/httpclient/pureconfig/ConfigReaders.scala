package com.avast.sst.jdk.httpclient.pureconfig

import pureconfig.ConfigReader
import com.avast.sst.jdk.httpclient.JdkHttpClientConfig

trait ConfigReaders {

  implicit val jdkHttpClientConfigReader: ConfigReader[JdkHttpClientConfig] = implicitly[ConfigReader[JdkHttpClientConfig]]

}
