---
layout: docs
title: "Doobie"
---

# Doobie

`libraryDependencies += "com.avast" %% "sst-doobie-hikari" % "@VERSION@"`

This subproject initializes a doobie `Transactor`:

```scala mdoc:silent
import cats.effect.Resource
import com.avast.sst.doobie.DoobieHikariModule
import com.avast.sst.example.config.Configuration
import com.avast.sst.jvm.execution.ConfigurableThreadFactory.Config
import com.avast.sst.jvm.execution.{ConfigurableThreadFactory, ExecutorModule}
import com.avast.sst.micrometer.jmx.MicrometerJmxModule
import com.avast.sst.pureconfig.PureConfigModule
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory
import scala.concurrent.ExecutionContext
import zio._
import zio.interop.catz._

implicit val runtime = new DefaultRuntime {} // this is just needed in example

for {
  configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
  executorModule <- ExecutorModule.makeFromExecutionContext[Task](runtime.platform.executor.asEC)
  meterRegistry <- MicrometerJmxModule.make[Task](configuration.jmx)
  boundedConnectExecutionContext <- executorModule
                                     .makeThreadPoolExecutor(
                                       configuration.boundedConnectExecutor,
                                       new ConfigurableThreadFactory(Config(Some("hikari-connect-%02d")))
                                     )
                                     .map(ExecutionContext.fromExecutorService)
  hikariMetricsFactory = new MicrometerMetricsTrackerFactory(meterRegistry)
  doobieTransactor <- DoobieHikariModule
                       .make[Task](configuration.database, boundedConnectExecutionContext, executorModule.blocker, Some(hikariMetricsFactory))
} yield doobieTransactor
```
