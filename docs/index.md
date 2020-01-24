\# Scala Server Toolkit Documentation

* [Getting Started](#getting-started)
* [Rationale](rationale.md)
* [Subproject Structure](#subproject-structure)
* [Bundles](#bundles)
* [http4s](http4s.md)
* [JVM](jvm.md)
* [Micrometer](micrometer.md)
* [PureConfig](pureconfig.md)
* [Datastax Cassandra Driver](cassandra-datastax-driver.md)
* [SSL Config](ssl-config.md)
* [doobie](doobie.md)
* [Flyway](flyway.md)
* [monix-catnap - CircuitBreaker](monix-catnap.md)

## Getting Started

Creating a simple HTTP server using [http4s](https://http4s.org) and [ZIO](https://zio.dev) is as easy as this:

#### build.sbt

[![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-bundle-zio-http4s-blaze_2.12)](https://repo1.maven.org/maven2/com/avast/sst-bundle-zio-http4s-blaze_2.12/)

`libraryDependencies += "com.avast" %% "sst-bundle-zio-http4s-blaze" % "<VERSION>"`

#### Main

```scala
import cats.effect._
import com.avast.sst.http4s.client._
import com.avast.sst.http4s.server._
import com.avast.sst.jvm.execution.ExecutorModule
import com.avast.sst.jvm.system.console.ConsoleModule
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import zio.DefaultRuntime
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.Task

implicit val runtime = new DefaultRuntime {} // this is just needed in example

val dsl = Http4sDsl[Task] // this is just needed in example
import dsl._

val routes = Http4sRouting.make { 
  HttpRoutes.of[Task] {
    case GET -> Root / "hello" => Ok("Hello World!")
  }
}

val resource = for {
  executorModule <- ExecutorModule.makeDefault[Task]
  console = ConsoleModule.make[Task]
  server <- Http4sBlazeServerModule.make[Task](Http4sBlazeServerConfig("127.0.0.1", 0), routes, executorModule.executionContext)
  client <- Http4sBlazeClientModule.make[Task](Http4sBlazeClientConfig(), executorModule.executionContext)
} yield (server, client, console)

val program = resource
  .use {
    case (server, client, console) =>
      client
        .expect[String](s"http://127.0.0.1:${server.address.getPort}/hello")
        .flatMap(console.printLine)
  }
```

```scala
runtime.unsafeRun(program)
// Hello World!
```

## Subproject Structure

The project is split into many small subprojects based on dependencies. For example code related to loading of configuration files via
[PureConfig](https://pureconfig.github.io) lives in a subproject named `sst-pureconfig` and code related to http4s server implemented using
[Blaze](https://github.com/http4s/blaze) lives in a subproject named `sst-http4s-server-blaze`.

There are also subprojects that implement interoperability between usually two dependencies. For example we want to configure our HTTP server
using PureConfig so definition of `implicit` `ConfigReader` instances live in subproject named `sst-http4s-server-blaze-pureconfig`. Or to give
another example, monitoring of HTTP server using [Micrometer](https://micrometer.io) lives in subproject named `sst-http4s-server-micrometer`.
Note that such subproject depends on APIs of both http4s server and Micrometer but it does not depend on concrete implementation which allows
you to choose any http4s implementation (Blaze, ...) and any Micrometer implementation (JMX, StatsD, ...).

## Bundles

Having many small and independent subprojects is great but in practice everyone wants to use certain combination of dependencies and does not
want to worry about many small dependencies. There are "bundles" for such use case - either the ones provided by this project or custom
ones created by the user.

One of the main decisions (dependency-wise) is to choose the effect data type. This project does not force you into specific data type and
supports both [ZIO](https://zio.dev) and [Monix](https://monix.io) out-of-the-box. So there are two main bundles one for each effect data
type that also bring in http4s server/client (Blaze), PureConfig and Micrometer.

Unless you have specific needs take one of these bundles and write your server application using them - it will be the simplest way.
