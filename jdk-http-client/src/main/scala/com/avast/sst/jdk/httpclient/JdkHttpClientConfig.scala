package com.avast.sst.jdk.httpclient

import java.net.http.HttpClient
import scala.concurrent.duration.FiniteDuration

final case class JdkHttpClientConfig(
    connectTimeout: Option[FiniteDuration] = None,
    followRedirects: Option[HttpClient.Redirect] = None,
    version: Option[HttpClient.Version] = None,
    priority: Option[Int] = None
)
