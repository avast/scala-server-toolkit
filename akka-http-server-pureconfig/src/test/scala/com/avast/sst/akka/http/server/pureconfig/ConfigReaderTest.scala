package com.avast.sst.akka.http.server.pureconfig

import akka.stream.TLSClientAuth
import cats.effect.IO
import com.avast.sst.akka.http.server.config.{AkkaHttpServerConfig, AkkaHttpServerConnectionContextConfig}
import com.avast.sst.pureconfig.PureConfigModule
import org.scalatest.funsuite.AsyncFunSuite
import pureconfig.generic.semiauto.deriveReader
import pureconfig.{ConfigReader, ConfigSource}

import scala.concurrent.duration._

final case class RootConfig(akkaHttpServer: AkkaHttpServerConfig)

class ConfigReaderTest extends AsyncFunSuite {

  import com.avast.sst.akka.http.server.pureconfig.implicits._ // import on this line is incorrectly reported as unused
  implicit val rootConfigReader: ConfigReader[RootConfig] = deriveReader

  test("Basic configuration") {
    val configSource = ConfigSource.resources("test-basic.conf")
    val expectedConfig = AkkaHttpServerConfig(
      listenHostname = "localhost",
      listenPort = 80,
      connectionContext = AkkaHttpServerConnectionContextConfig.NoEncryption,
      startupTimeout = 10.seconds,
      shutdownTimeout = 10.seconds
    )

    PureConfigModule.make[IO, RootConfig](configSource).map { config =>
      assert(config === Right(RootConfig(expectedConfig)))
    }.unsafeToFuture()
  }

  test("HTTPS configuration") {
    val configSource = ConfigSource.resources("test-https.conf")
    val expectedConfig = AkkaHttpServerConfig(
      listenHostname = "localhost",
      connectionContext = AkkaHttpServerConnectionContextConfig.Https(
        enabledCipherSuites = Some(Vector("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256")),
        enabledProtocols = Some(Vector("TLSv1.2")),
        clientAuth = Some(TLSClientAuth.Need)
      )
    )

    PureConfigModule.make[IO, RootConfig](configSource).map { config =>
      assert(config === Right(RootConfig(expectedConfig)))
    }.unsafeToFuture()
  }

}
