---
layout: docs
title: "http4s"
---

# http4s

`libraryDependencies += "com.avast" %% "sst-http4s-server-blaze" % "@VERSION@"`

There are multiple `http4s-*` subprojects that provide easy initialization of a server and a client. Http4s is an interface with multiple possible 
implementations - for now we provide only implementations based on [Blaze](https://github.com/http4s/blaze).

Both server and client are configured via configuration `case class` which contains default values taken from the underlying implementations.

```scala mdoc:silent:reset-class
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

```scala mdoc
runtime.unsafeRun(program)
```

## Middleware

### Correlation ID Middleware

```scala mdoc:silent:reset
import cats.effect._
import com.avast.sst.http4s.server._
import com.avast.sst.http4s.server.middleware.CorrelationIdMiddleware
import com.avast.sst.jvm.execution.ExecutorModule
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import zio.DefaultRuntime
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.Task

val dsl = Http4sDsl[Task]  // this is just needed in example
import dsl._

implicit val runtime = new DefaultRuntime {}  // this is just needed in example

for {
  middleware <- Resource.liftF(CorrelationIdMiddleware.default[Task])
  executorModule <- ExecutorModule.makeDefault[Task]
  routes = Http4sRouting.make {
    middleware.wrap {
      HttpRoutes.of[Task] {
        case GET -> Root =>
          // val correlationId = middleware.retrieveCorrelationId(req)
          ???
      }
    }
  }
  server <- Http4sBlazeServerModule.make[Task](Http4sBlazeServerConfig.localhost8080, routes, executorModule.executionContext)
} yield server
```

## Circuit Breaker

It is a good practice to wrap any communication with external system with circuit breaking mechanism to prevent spreading of errors and
bad latency. See [monix-catnap](monix-catnap.md) for one of the options.
