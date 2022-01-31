package com.avast.sst.pureconfig

import cats.data.NonEmptyList
import cats.effect.SyncIO
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AnyFunSuite
import pureconfig.error.ConfigReaderException
import pureconfig.generic.semiauto.deriveReader
import pureconfig.{ConfigReader, ConfigSource}

class PureConfigModuleTest extends AnyFunSuite {

  private val config = ConfigFactory.parseString("""|number = 123
                                                    |string = "test"
                                                    |test-object {
                                                    |   some-key = "value1"
                                                    |} """.stripMargin)

  private case class TestConfig(number: Int, string: String, testObject: TestObject)
  private case class TestObject(someKey: String)

  implicit private val testConfigReader: ConfigReader[TestConfig] = deriveReader
  implicit private val objectConfigReader: ConfigReader[TestObject] =
    deriveReader

  private val source = ConfigSource.fromConfig(config)

  test("Simple configuration loading") {
    assert(
      PureConfigModule
        .make[SyncIO, TestConfig](source)
        .unsafeRunSync() === Right(TestConfig(123, "test", TestObject("value1")))
    )
    assert(
      PureConfigModule
        .make[SyncIO, TestConfig](ConfigSource.empty)
        .unsafeRunSync() === Left(
        NonEmptyList(
          "Invalid configuration : Key not found: 'number'.",
          List("Invalid configuration : Key not found: 'string'.", "Invalid configuration : Key not found: 'test-object'.")
        )
      )
    )
  }

  test("Configuration loading with exceptions") {
    assert(
      PureConfigModule
        .makeOrRaise[SyncIO, TestConfig](source)
        .unsafeRunSync() === TestConfig(123, "test", TestObject("value1"))
    )
    assertThrows[ConfigReaderException[TestConfig]] {
      PureConfigModule
        .makeOrRaise[SyncIO, TestConfig](ConfigSource.empty)
        .unsafeRunSync()
    }
  }

  test("Configuration loading with environment variable override") {
    val env = Map("PREFIX__TEST_OBJECT__SOME_KEY" -> "value2")
    val conf = PureConfigModule
      .makeWithEnvironmentVariables[SyncIO, TestConfig]("PREFIX", env = env, config = config)
      .unsafeRunSync()

    assert(conf === Right(TestConfig(123, "test", TestObject("value2"))))
  }

  test("Configuration loading with config file override") {
    val env = Map("SERVER_FLAG" -> "special_config")
    val conf = PureConfigModule
      .makeWithMultipleFiles[SyncIO, TestConfig](List("SERVER_FLAG"), env = env, config = config)
      .unsafeRunSync()

    assert(conf === Right(TestConfig(123, "test", TestObject("value3"))))
  }

  test("Configuration loading with config file override and environment variable override") {
    val env =
      Map("SERVER_FLAG" -> "special_config", "PREFIX__STRING" -> "test2")
    val conf = PureConfigModule
      .makeWithFallbacks[SyncIO, TestConfig](List("SERVER_FLAG"), Some("PREFIX"), env = env, config = config)
      .unsafeRunSync()

    assert(conf === Right(TestConfig(123, "test2", TestObject("value3"))))
  }
}
