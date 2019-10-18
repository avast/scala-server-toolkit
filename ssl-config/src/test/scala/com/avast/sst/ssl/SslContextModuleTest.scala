package com.avast.sst.ssl

import cats.effect.SyncIO
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite

class SslContextModuleTest extends FunSuite {

  test("SslContextModule initializes properly from JKS store") {
    val sslContext = SslContextModule.make[SyncIO](ConfigFactory.load().getConfig("ssl-config")).unsafeRunSync()
    assert(sslContext.getProtocol === "TLSv1.2")
  }

}
