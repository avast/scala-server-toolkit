# Doobie

[![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-doobie-hikari_2.12)](https://repo1.maven.org/maven2/com/avast/sst-doobie-hikari_2.12/)

`libraryDependencies += "com.avast" %% "sst-doobie-hikari" % "<VERSION>"`

This module initializes a doobie `Transactor`:

```scala
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
import zio.interop.catz.implicits._

implicit val runtime = new DefaultRuntime {} // this is just needed in example

for {
  configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
  executorModule <- ExecutorModule.makeFromExecutionContext[Task](runtime.Platform.executor.asEC)
  meterRegistry <- MicrometerJmxModule.make[Task](configuration.jmx)
  boundedConnectExecutionContext <- executorModule
                                     .makeThreadPoolExecutor(
                                       configuration.boundedConnectExecutor,
                                       new ConfigurableThreadFactory(Config(Some("hikari-connect-%02d")))
                                     )
                                     .map(ExecutionContext.fromExecutorService)
  hikariMetricsFactory = new MicrometerMetricsTrackerFactory(meterRegistry)
  doobieTransactor <- DoobieHikariModule
                       .make[Task](configuration.database, boundedConnectExecutionContext, executorModule.blocker, hikariMetricsFactory)
} yield doobieTransactor
```

