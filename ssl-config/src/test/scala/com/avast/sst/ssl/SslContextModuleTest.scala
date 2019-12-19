package com.avast.sst.ssl

import cats.effect.SyncIO
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite

class SslContextModuleTest extends AnyFunSuite {

  test("SslContextModule initializes properly from JKS store with reference config") {
    val sslContext = SslContextModule.make[SyncIO](ConfigFactory.empty()).unsafeRunSync()
    assert(sslContext.getProtocol === "TLSv1.2")
  }

  test("SslContextModule initializes properly from JKS store with provided config") {
    val sslContext = SslContextModule.make[SyncIO](ConfigFactory.load().getConfig("ssl-config"), withReference = false).unsafeRunSync()
    assert(sslContext.getProtocol === "TLSv1.2")
  }

  test("SslContextModule fails to initialize for empty config and no reference config") {
    val result = SslContextModule.make[SyncIO](ConfigFactory.empty(), withReference = false).attempt.unsafeRunSync()
    assert(result.isLeft)
  }

}
