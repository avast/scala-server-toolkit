# Module http4s

[![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-http4s-server-blaze_2.12)](https://repo1.maven.org/maven2/com/avast/sst-http4s-server-blaze_2.12/)

`libraryDependencies += "com.avast" %% "sst-http4s-server-blaze" % "<VERSION>"`

There are `http4s-*` modules that provide easy initialization of a server and a client. Http4s is an interface with multiple possible
implementations - for now we provide only implementations based on [Blaze](https://github.com/http4s/blaze).

Both server and client are configured via configuration `case class` which contains default values taken from the underlying implementations.

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
  client <- Http4sBlazeClient.make[Task](Http4sBlazeClientConfig(), executorModule.executionContext)
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

## Middleware

### Correlation ID Middleware

```scala
import cats.effect._
import com.avast.sst.jvm.execution.ExecutorModule
import com.avast.sst.http4s.server._
import com.avast.sst.http4s.server.middleware.CorrelationIdMiddleware
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

