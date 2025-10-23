package com.avast.sst.pureconfig

import cats.data.NonEmptyList
import cats.effect.SyncIO
import org.scalatest.funsuite.AnyFunSuite
import pureconfig.error.ConfigReaderException
import pureconfig.generic.semiauto.*
import pureconfig.{ConfigReader, ConfigSource}

import scala.annotation.nowarn

@nowarn("msg=unused value")
class PureConfigModuleTest extends AnyFunSuite {

  private val source = ConfigSource.string("""|number = 123
                                              |string = "test"""".stripMargin)

  private val sourceWithMissingField = ConfigSource.string("number = 123")

  private val sourceWithTypeError = ConfigSource.string("""|number = wrong_type
                                                           |string = "test"""".stripMargin)

  final private case class TestConfig(number: Int, string: String)

  implicit private val configReader: ConfigReader[TestConfig] = deriveReader[TestConfig]

  test("Simple configuration loading") {
    assert(PureConfigModule.make[SyncIO, TestConfig](source).unsafeRunSync() === Right(TestConfig(123, "test")))
    assert(
      PureConfigModule.make[SyncIO, TestConfig](ConfigSource.empty).unsafeRunSync() === Left(
        NonEmptyList(
          "Invalid configuration  @ empty config: Key not found: 'number'.",
          List("Invalid configuration  @ empty config: Key not found: 'string'.")
        )
      )
    )
    assert(
      PureConfigModule.make[SyncIO, TestConfig](sourceWithMissingField).unsafeRunSync() === Left(
        NonEmptyList(
          "Invalid configuration  @ String: 1: Key not found: 'string'.",
          List.empty
        )
      )
    )
    assert(
      PureConfigModule.make[SyncIO, TestConfig](sourceWithTypeError).unsafeRunSync() === Left(
        NonEmptyList(
          "Invalid configuration number @ String: 1: Expected type NUMBER. Found STRING instead.",
          List.empty
        )
      )
    )
  }

  test("Configuration loading with exceptions") {
    assert(PureConfigModule.makeOrRaise[SyncIO, TestConfig](source).unsafeRunSync() === TestConfig(123, "test"))
    assertThrows[ConfigReaderException[TestConfig]] {
      PureConfigModule.makeOrRaise[SyncIO, TestConfig](ConfigSource.empty).unsafeRunSync()
    }
    assertThrows[ConfigReaderException[TestConfig]] {
      PureConfigModule.makeOrRaise[SyncIO, TestConfig](sourceWithMissingField).unsafeRunSync()
    }
    assertThrows[ConfigReaderException[TestConfig]] {
      PureConfigModule.makeOrRaise[SyncIO, TestConfig](sourceWithTypeError).unsafeRunSync()
    }
  }

}
