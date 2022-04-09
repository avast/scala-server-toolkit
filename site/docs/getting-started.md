---
layout: docs
title: "Getting Started"
position: 0
---

# Getting Started

Creating a simple HTTP server using [http4s](https://http4s.org) and [ZIO](https://zio.dev) is as easy as this:

### build.sbt

`libraryDependencies += "com.avast" %% "sst-bundle-zio-http4s-blaze" % "@VERSION@"`

### Main

```scala mdoc:silent:reset-class
import cats.effect._
import com.avast.sst.http4s.client._
import com.avast.sst.http4s.server._
import com.avast.sst.jvm.execution.ExecutorModule
import com.avast.sst.jvm.system.console.ConsoleModule
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.{Task, ZEnv, Runtime}

implicit val runtime: Runtime[ZEnv] = zio.Runtime.default // this is just needed in example

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

```scala mdoc
runtime.unsafeRun(program)
```

Or you can use the [official Giter8 template](https://github.com/avast/sst-seed.g8):

```bash
sbt new avast/sst-seed.g8
```
