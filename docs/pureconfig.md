# Module PureConfig

![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/avast/scala-server-toolkit?label=version&sort=semver)

`libraryDependencies += "com.avast.server.toolkit" %% "scala-server-toolkit-pureconfig" % "<VERSION>"`

This module allows you to load your application's configuration file according to a case class provided to it. It uses
[PureConfig](https://pureconfig.github.io) library to do so which uses [Lightbend Config](https://github.com/lightbend/config) which means
that your application's configuration will be in [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md) format.


```scala
import com.avast.server.toolkit.pureconfig._
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader
import zio.interop.catz._
import zio.Task

final case class ServerConfiguration(listenAddress: String, listenPort: Int)

implicit val serverConfigurationReader: ConfigReader[ServerConfiguration] = deriveReader
// serverConfigurationReader: ConfigReader[ServerConfiguration] = pureconfig.generic.DerivedConfigReader1$$anon$3@662e5590

val maybeConfiguration = PureConfigModule.make[Task, ServerConfiguration]
// maybeConfiguration: Task[Either[cats.data.NonEmptyList[String], ServerConfiguration]] = zio.ZIO$EffectPartial@606f0f70
```

