package com.avast.server.toolkit.ssl

import java.nio.file.Paths

import cats.effect.SyncIO
import org.scalatest.FunSuite

class SslContextModuleTest extends FunSuite {

  test("SslContextModule") {
    val truststore = getClass.getResource("/truststore.jks").toURI.getPath
    val sslContext = SslContextModule
      .make[SyncIO](
        SslContextConfig(truststore = Some(KeyStoreConfig(KeyStoreType.JKS, Paths.get(truststore), "CanNotMakeJKSWithoutPass", None)))
      )
      .unsafeRunSync()
    assert(sslContext.getProtocol === "TLS")
  }

}
