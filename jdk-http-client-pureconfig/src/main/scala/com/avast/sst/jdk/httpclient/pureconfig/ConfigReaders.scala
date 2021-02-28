package com.avast.sst.jdk.httpclient.pureconfig

import com.avast.sst.jdk.httpclient.JdkHttpClientConfig
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader

import java.net.http.HttpClient

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val jdkHttpClientVersionConfigReader: ConfigReader[HttpClient.Version] = ConfigReader.stringConfigReader.emap {
    case "HTTP_1_1" => Right(HttpClient.Version.HTTP_1_1)
    case "HTTP_2"   => Right(HttpClient.Version.HTTP_2)
    case value      => Left(CannotConvert(value, "java.net.http.HttpClient.Version", ""))
  }

  implicit val jdkHttpClientRedirectConfigReader: ConfigReader[HttpClient.Redirect] = ConfigReader.stringConfigReader.emap {
    case "NEVER"  => Right(HttpClient.Redirect.NEVER)
    case "ALWAYS" => Right(HttpClient.Redirect.ALWAYS)
    case "NORMAL" => Right(HttpClient.Redirect.NORMAL)
    case value    => Left(CannotConvert(value, "java.net.http.HttpClient.Redirect", ""))
  }

  implicit val jdkHttpClientConfigReader: ConfigReader[JdkHttpClientConfig] = deriveReader

}
