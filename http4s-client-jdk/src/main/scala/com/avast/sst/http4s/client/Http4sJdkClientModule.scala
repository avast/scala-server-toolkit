package com.avast.sst.http4s.client

import cats.effect.{ConcurrentEffect, Resource}
import javax.net.ssl.SSLContext
import org.http4s.client.Client

import scala.concurrent.ExecutionContext
import org.http4s.client.jdkhttpclient.JdkHttpClient
import cats.effect.ContextShift
import java.net.http.HttpClient

object Http4sJdkClientModule {

  /** Makes [[org.http4s.client.Client]] (Blaze) initialized with the given config.
    *
    * @param executionContext callback handling [[scala.concurrent.ExecutionContext]]
    */
  def make[F[_]: ConcurrentEffect: ContextShift](
      config: Http4sJdkClientConfig,
      executionContext: ExecutionContext,
      sslContext: Option[SSLContext] = None
  ): Resource[F, Client[F]] = {
    HttpClient.newBuilder()
    JdkHttpClient(???)
    ???
  }

}
