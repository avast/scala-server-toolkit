package com.avast.sst.akka.http.server.config

import javax.net.ssl.{SSLContext, SSLParameters}

final case class AkkaHttpServerSslConfig(sslContext: SSLContext, sslParameters: Option[SSLParameters])
