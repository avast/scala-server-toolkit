package com.avast.sst.jdk.httpclient

import java.net.http.HttpClient
import java.net.{Authenticator, CookieHandler, ProxySelector}
import java.time.{Duration => JDuration}
import java.util.concurrent.Executor
import javax.net.ssl.SSLContext

object JdkHttpClientModule {

  /** Makes [[java.net.http.HttpClient]] initialized with the given config. */
  def make(
      config: JdkHttpClientConfig,
      executor: Option[Executor] = None,
      sslContext: Option[SSLContext] = None,
      cookieHandler: Option[CookieHandler] = None,
      proxySelector: Option[ProxySelector] = None,
      authenticator: Option[Authenticator] = None
  ): HttpClient = {
    val builder = HttpClient.newBuilder()

    config.connectTimeout.map(d => JDuration.ofNanos(d.toNanos)).foreach(builder.connectTimeout)
    executor.foreach(builder.executor)
    sslContext.foreach(builder.sslContext)
    config.followRedirects.foreach(builder.followRedirects)
    config.priority.foreach(builder.priority)
    cookieHandler.foreach(builder.cookieHandler)
    proxySelector.foreach(builder.proxy)
    authenticator.foreach(builder.authenticator)

    builder.build()
  }

}
