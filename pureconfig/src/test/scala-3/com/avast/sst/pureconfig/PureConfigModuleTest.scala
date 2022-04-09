package com.avast.sst.pureconfig

import cats.data.NonEmptyList
import cats.effect.SyncIO
import org.scalatest.funsuite.AnyFunSuite
import pureconfig.error.ConfigReaderException
import pureconfig.{ConfigReader, ConfigSource}
import pureconfig.generic.derivation.default._

class PureConfigModuleTest extends AnyFunSuite {

  private val source = ConfigSource.string("""|number = 123
                                              |string = "test"""".stripMargin)

  private case class TestConfig(number: Int, string: String)

  implicit private val configReader: ConfigReader[TestConfig] = ConfigReader.derived

  test("Simple configuration loading") {
    assert(PureConfigModule.make[SyncIO, TestConfig](source).unsafeRunSync() === Right(TestConfig(123, "test")))
    assert(
      PureConfigModule.make[SyncIO, TestConfig](ConfigSource.empty).unsafeRunSync() === Left(
        NonEmptyList(
          "Invalid configuration number: Key not found: 'number'.",
          List("Invalid configuration string: Key not found: 'string'.")
        )
      )
    )
  }

  test("Configuration loading with exceptions") {
    assert(PureConfigModule.makeOrRaise[SyncIO, TestConfig](source).unsafeRunSync() === TestConfig(123, "test"))
    assertThrows[ConfigReaderException[TestConfig]] {
      PureConfigModule.makeOrRaise[SyncIO, TestConfig](ConfigSource.empty).unsafeRunSync()
    }
  }

}
