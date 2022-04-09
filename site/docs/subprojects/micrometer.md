---
layout: docs
title: "Micrometer"
---

# Micrometer

`libraryDependencies += "com.avast" %% "sst-micrometer-jmx" % "@VERSION@"`

This subproject allows you to monitor your applications using [Micrometer](https://micrometer.io). There are many actual implementations of 
the Micrometer API one of which is JMX. Subproject `sst-micrometer-jmx` implements the initialization of Micrometer for JMX. There are also
interop subprojects such as `sst-http4s-server-micrometer` which implement monitoring of HTTP server and individual routes using Micrometer.

```scala mdoc:silent
import cats.effect.{Clock, Resource}
import com.avast.sst.http4s.server.*
import com.avast.sst.http4s.server.micrometer.MicrometerHttp4sServerMetricsModule
import com.avast.sst.jvm.execution.ExecutorModule
import com.avast.sst.jvm.micrometer.MicrometerJvmModule
import com.avast.sst.micrometer.jmx.*
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.server.Server
import zio.interop.catz.*
import zio.interop.catz.implicits.*
import zio.*

implicit val runtime: Runtime[ZEnv] = zio.Runtime.default // this is just needed in example

val dsl = Http4sDsl[Task] // this is just needed in example
import dsl.*

for {
  executorModule <- ExecutorModule.makeFromExecutionContext[Task](runtime.platform.executor.asEC)
  clock = Clock.create[Task]
  jmxMeterRegistry <- MicrometerJmxModule.make[Task](MicrometerJmxConfig("com.avast"))
  _ <- Resource.eval(MicrometerJvmModule.make[Task](jmxMeterRegistry))
  serverMetricsModule <- Resource.eval(MicrometerHttp4sServerMetricsModule.make[Task](jmxMeterRegistry, executorModule.blocker, clock))
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
