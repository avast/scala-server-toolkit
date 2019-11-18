# Modules Micrometer

[![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-micrometer-jmx_2.12)](https://repo1.maven.org/maven2/com/avast/sst-micrometer-jmx_2.12/)

`libraryDependencies += "com.avast" %% "sst-micrometer-jmx" % "<VERSION>"`

This module allows you to monitor your applications using [Micrometer](https://micrometer.io). There are many actual implementations of 
the Micrometer API one of which is JMX. Module `sst-micrometer-jmx` implements the initialization of Micrometer for JMX. There are also
interop modules such as `sst-http4s-server-micrometer` which implement monitoring of HTTP server and individual routes using Micrometer.

```scala mdoc:silent
import cats.effect.{Clock, Resource}
import com.avast.sst.http4s.server._
import com.avast.sst.http4s.server.micrometer.MicrometerHttp4sServerMetricsModule
import com.avast.sst.jvm.execution.ExecutorModule
import com.avast.sst.jvm.micrometer.MicrometerJvmModule
import com.avast.sst.micrometer.jmx._
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.server.Server
import zio.DefaultRuntime
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.Task

implicit val runtime = new DefaultRuntime {} // this is just needed in example

val dsl = Http4sDsl[Task] // this is just needed in example
import dsl._

for {
  executorModule <- ExecutorModule.makeFromExecutionContext[Task](runtime.platform.executor.asEC)
  clock = Clock.create[Task]
  jmxMeterRegistry <- MicrometerJmxModule.make[Task](MicrometerJmxConfig("com.avast"))
  _ <- Resource.liftF(MicrometerJvmModule.make[Task](jmxMeterRegistry))
  serverMetricsModule <- Resource.liftF(MicrometerHttp4sServerMetricsModule.make[Task](jmxMeterRegistry, clock))
  routes = Http4sRouting.make {
    serverMetricsModule.serverMetrics {
      HttpRoutes.of[Task] {
        case GET -> Root / "hello" => Ok("Hello World!")
      }
    } 
  }
  server <- Http4sBlazeServerModule.make[Task](Http4sBlazeServerConfig("127.0.0.1", 0), routes, executorModule.executionContext)
} yield server
```
