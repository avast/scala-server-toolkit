package com.avast.sst.pureconfig

import cats.data.NonEmptyList
import cats.effect.Sync
import cats.syntax.either._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.typesafe.config.{Config, ConfigFactory}
import pureconfig.error.{ConfigReaderFailure, ConfigReaderFailures, ConvertFailure}
import pureconfig.{CamelCase, ConfigReader, ConfigSource, KebabCase, NamingConvention}

import java.util.Properties
import scala.reflect.ClassTag

/** Provides loading of configuration into case class via PureConfig. */
object PureConfigModule {

  /** Loads the case class `A` using Lightbend Config's standard behavior. */
  def make[F[_]: Sync, A: ConfigReader]: F[Either[NonEmptyList[String], A]] =
    make(ConfigSource.default)

  /** Loads the case class `A` using provided [[pureconfig.ConfigSource]]. */
  def make[F[_]: Sync, A: ConfigReader](source: ConfigSource): F[Either[NonEmptyList[String], A]] = {
    Sync[F].delay(source.load[A].leftMap(convertFailures))
  }

  /** Loads the case class `A` using Lightbend Config's standard behavior or raises an exception. */
  def makeOrRaise[F[_]: Sync, A: ConfigReader: ClassTag]: F[A] =
    makeOrRaise(ConfigSource.default)

  /** Loads the case class `A` using provided [[pureconfig.ConfigSource]] or raises an exception. */
  def makeOrRaise[F[_]: Sync, A: ConfigReader: ClassTag](source: ConfigSource): F[A] = Sync[F].delay(source.loadOrThrow[A])

  /** Loads the case class `A` using Lightbend Config's standard behavior with possible override from files selected via environment
    * variables (`configNames`)`.`
    */
  def makeWithMultipleFiles[F[_]: Sync, A: ConfigReader](
      configNames: List[String],
      env: Map[String, String] = sys.env,
      namingConventions: Set[NamingConvention] = Set(KebabCase, CamelCase),
      config: Config = ConfigFactory.load()
  ): F[Either[NonEmptyList[String], A]] = {
    makeWithFallbacks(configNames, None, env, namingConventions, config)
  }

  /** Loads the case class `A` using Lightbend Config's standard behavior with possible override from environment variables with `prefix`.
    * Namespaces are separated by two underscores __ and name parts using single underscore. Example: "MY_PREFIX__HTTP_CLIENT__TIMEOUT" is
    * equal to "httpClient.timeout" when camelCase formatting is used.
    */
  def makeWithEnvironmentVariables[F[_]: Sync, A: ConfigReader](
      prefix: String,
      env: Map[String, String] = sys.env,
      namingConventions: Set[NamingConvention] = Set(KebabCase, CamelCase),
      config: Config = ConfigFactory.load()
  ): F[Either[NonEmptyList[String], A]] = {
    makeWithFallbacks(List.empty, Some(prefix), env, namingConventions, config)
  }

  def makeWithFallbacks[F[_]: Sync, A: ConfigReader](
      configNames: List[String],
      prefix: Option[String],
      env: Map[String, String] = sys.env,
      namingConventions: Set[NamingConvention] = Set(KebabCase, CamelCase),
      config: Config = ConfigFactory.load()
  ): F[Either[NonEmptyList[String], A]] = {
    for {
      finalConfig <- Sync[F].delay(loadAndCombineConfigs(configNames, prefix, env, namingConventions, config))
      result <- make[F, A](ConfigSource.fromConfig(finalConfig))
    } yield result
  }

  private def convertFailures(failures: ConfigReaderFailures): NonEmptyList[String] = {
    NonEmptyList(failures.head, failures.tail.toList).map(formatFailure)
  }

  private def formatFailure(configReaderFailure: ConfigReaderFailure): String = {
    configReaderFailure match {
      case convertFailure: ConvertFailure =>
        s"Invalid configuration ${convertFailure.path}: ${convertFailure.description}"
      case configFailure =>
        s"Invalid configuration : ${configFailure.description}"
    }
  }

  private def loadAndCombineConfigs(
      configNames: List[String],
      prefix: Option[String],
      env: Map[String, String],
      namingConventions: Set[NamingConvention],
      config: Config
  ): Config = {
    val defaultConfig = ConfigFactory.load(config)
    val additionalConfigs = loadAdditionalConfigs(configNames, env)
    val environmentConfig =
      loadEnvironmentConfig(prefix, env, namingConventions)

    environmentConfig.withFallback(additionalConfigs.withFallback(defaultConfig))
  }

  private def loadAdditionalConfigs(configNames: List[String], env: Map[String, String]): Config = {
    configNames.foldLeft(ConfigFactory.empty()) { (config, key) =>
      env
        .get(key)
        .map(value => ConfigFactory.parseResources(s"$value.conf"))
        .fold(config)(config.withFallback(_))
    }
  }

  private def loadEnvironmentConfig(prefix: Option[String], env: Map[String, String], namingConventions: Set[NamingConvention]): Config = {
    prefix match {
      case Some(value) =>
        namingConventions.foldLeft(ConfigFactory.empty()) { (cfg, namingConvention) =>
          cfg.withFallback(environmentConfig(value, env, namingConvention))
        }
      case None => ConfigFactory.empty()
    }

  }

  /** Parses map where keys are configuration options, and values are config values. Namespaces are separated by two underscores __ and name
    * parts using single underscore. Example: MY_PREFIX__HTTP_CLIENT__TIMEOUT is equal to httpClient.timeout when camelCase formatting is
    * used.
    */
  def environmentConfig(prefix: String, environment: Map[String, String], namingConvention: NamingConvention): Config = {
    val parsedPrefix =
      namingConvention.toTokens(prefix).mkString("_").toUpperCase
    val filteredEnvironment =
      environment.view.filter(_._1.startsWith(parsedPrefix))

    val properties = new Properties()
    filteredEnvironment.foreach { case (name, value) =>
      val namespaces = name.stripPrefix(s"${prefix}__").split("__")
      val namespacesCase = namespaces
        .map(_.toLowerCase.split('_').toList)
        .map(namingConvention.fromTokens)
      properties.setProperty(namespacesCase.mkString("."), value)
    }
    ConfigFactory.parseProperties(properties)
  }
}
