---
layout: docs
title: "SSL Config"
---

# SSL Config

`libraryDependencies += "com.avast" %% "sst-ssl-config" % "@VERSION@"`

This subproject allows you to create SSL context (`javax.net.ssl.SSLContext`) from a configuration file. It uses [SSL Config](https://github.com/lightbend/ssl-config) 
library to do so which means that this module is different than others and receives directly `com.typesafe.config.Config` instead of 
configuration case classes. See the [documentation of SSL Config](https://lightbend.github.io/ssl-config) for more information. 

Loading of SSL context is side-effectful so it is wrapped in `F` which is `Sync`.

```scala mdoc:silent
import com.avast.sst.ssl.SslContextModule
import com.typesafe.config.ConfigFactory
import zio.interop.catz._
import zio.Task

val config = ConfigFactory.load().getConfig("ssl-config")
val sslContext = SslContextModule.make[Task](config)
```
