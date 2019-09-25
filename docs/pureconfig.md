# Module PureConfig

[![Maven Central](https://img.shields.io/maven-central/v/com.avast/scala-server-toolkit-pureconfig_2.12)](https://repo1.maven.org/maven2/com/avast/scala-server-toolkit-pureconfig_2.12/)

`libraryDependencies += "com.avast" %% "scala-server-toolkit-pureconfig" % "<VERSION>"`

This module allows you to load your application's configuration file according to a case class provided to it. It uses
[PureConfig](https://pureconfig.github.io) library to do so which uses [Lightbend Config](https://github.com/lightbend/config) which means
that your application's configuration will be in [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md) format.

Loading of configuration is side-effectful so it is wrapped in `F` which is `Sync`. This module also tweaks the error messages a little.

```scala
import com.avast.server.toolkit.pureconfig._
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader
import zio.interop.catz._
import zio.Task

final case class ServerConfiguration(listenAddress: String, listenPort: Int)

implicit val serverConfigurationReader: ConfigReader[ServerConfiguration] = deriveReader
// serverConfigurationReader: ConfigReader[ServerConfiguration] = pureconfig.generic.DerivedConfigReader1$$anon$3@2a8eed58

val maybeConfiguration = PureConfigModule.make[Task, ServerConfiguration]
// maybeConfiguration: Task[Either[cats.data.NonEmptyList[String], ServerConfiguration]] = zio.ZIO$EffectPartial@352bea0e
```

