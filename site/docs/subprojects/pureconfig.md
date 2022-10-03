---
layout: docs
title: "PureConfig"
---

# PureConfig

`libraryDependencies += "com.avast" %% "sst-pureconfig" % "@VERSION@"`

This subproject allows you to load your application's configuration file into a case class. It uses [PureConfig](https://pureconfig.github.io) 
library to do so. PureConfig uses [Lightbend Config](https://github.com/lightbend/config) which means that your application's configuration 
will be in [HOCON](https://github.com/lightbend/config/blob/master/HOCON.md) format.

Loading of a configuration is effectful, so it is wrapped in `F[_]` which is `Sync`. This module also tweaks the error messages a little.

## Usage and typeclass derivation
Unfortunately there are some incompatible changes between Scala 2 and Scala 3 series of PureConfig when it comes to typeclass derivation. We've created different implementation for each series in order to unblock migration to Scala 3 for SST users. Read on for further details on usage of each.

### Scala 2

Sample usage:

```scala
import com.avast.sst.pureconfig.PureConfigModule
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader
import zio.interop.catz.*
import zio.Task

final case class ServerConfiguration(listenAddress: String, listenPort: Int)

implicit val serverConfigurationReader: ConfigReader[ServerConfiguration] = deriveReader

val maybeConfiguration = PureConfigModule.make[Task, ServerConfiguration]
```

We also provide modules with implicits for many of SST modules. Look for `sst-*-pureconfig` subprojects to get `implicit` instances of `ConfigReader` for specific libraries, e.g.:

```scala mdoc:silent
import com.avast.sst.http4s.server.pureconfig.implicits.*
```

The default `*.pureconfig.implicits._` import contains the default naming convention for PureConfig which is `kebab-case` 
for the configuration file and `camelCase` for the case class field names. You can either choose a different naming convention by different
import (e.g. `*.pureconfig.implicits.CamelCase`) or you can [create your own](https://pureconfig.github.io/docs/overriding-behavior-for-case-classes.html#field-mappings)
and `extend` the `ConfigReaders` `trait`.

### Scala 3

Unlike for Scala 2, pureconfig in Scala 3 supports only `kebab-case` derivation. This is reflected in the structure of the implicits we provide. The derivation API has also changed. See [official docs](https://pureconfig.github.io/docs/scala-3-derivation.html) for more details.

Sample usage:

```scala mdoc:silent
import com.avast.sst.pureconfig.PureConfigModule
import pureconfig.ConfigReader
import zio.interop.catz.*
import zio.Task
import pureconfig.generic.derivation.default.*

final case class ServerConfiguration(listenAddress: String, listenPort: Int)

implicit val serverConfigurationReader: ConfigReader[ServerConfiguration] = ConfigReader.derived

val maybeConfiguration = PureConfigModule.make[Task, ServerConfiguration]
```


## Toggle
Sometimes it is useful to be able to disable some functionality via config. That usually means to add a new boolean field to particular configuration case class. You have to be aware of this new field when you work with the configuration and check the value of it to enable/disable particular functionality.

`Toggle` is a more safe approach for this pattern as it can be either `Enabled[T]` or `Disabled`.
So it will force you to deal with the possibility of disabled functionality.
It also simplifies the workflow and particular config case class as it does not require any changes to it.
The boolean flag is build in the `Toggle` itself and the value is converted to `Enabled[T]` or `Disabled`.
For more details please see the example below:


```
case class MyConfig(myService: Toggle[ServiceConfig])
case class ServiceConfig(url: String, timeout: Duration)
```

```
my-service {
    enabled = false
    url = "http://www.example.com/v1/some-endpoint"
    timeout = 5s
}
```


## WithConfig
Sometimes you need to load the configuration both as your custom configuration class and raw `Config` when you deal with legacy code. Using `WithConfig` case class allows you to load both objects simultaneously.

Example:

```
case class MyConfig(myService: WithConfig[ServiceConfig])
case class ServiceConfig(url: String, timeout: Duration)
```

```
val serviceConfig: Config = myConfig.myService.config
val urlFromConfig: String = serviceConfig.getString("url")

val serviceConfigCustom: ServiceConfig = myConfig.myService.value
val urlFromCustomObject: String = serviceConfigCustom.url
```
