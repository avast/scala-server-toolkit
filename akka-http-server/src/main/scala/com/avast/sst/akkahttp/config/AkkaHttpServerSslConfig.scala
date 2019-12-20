package com.avast.sst.akkahttp.config

import javax.net.ssl.{SSLContext, SSLParameters}

final case class AkkaHttpServerSslConfig(sslContext: SSLContext, sslParameters: Option[SSLParameters])
