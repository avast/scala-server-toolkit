package com.avast.sst.example

import java.util.concurrent.TimeUnit

import cats.effect.{Clock, Resource}
import com.avast.sst.bundle.ZioServerApp
import com.avast.sst.example.config.Configuration
import com.avast.sst.example.module.Http4sRoutingModule
import com.avast.sst.http4s.server.Http4sBlazeServerModule
import com.avast.sst.http4s.server.micrometer.MicrometerHttp4sServerMetricsModule
import com.avast.sst.jvm.execution.ExecutorModule
import com.avast.sst.jvm.micrometer.MicrometerJvmModule
import com.avast.sst.jvm.system.console.{Console, ConsoleModule}
import com.avast.sst.micrometer.jmx.MicrometerJmxModule
import com.avast.sst.pureconfig.PureConfigModule
import org.http4s.server.Server
import zio.Task
import zio.interop.catz._
import zio.interop.catz.implicits._

object Main extends ZioServerApp {

  def program: Resource[Task, Server[Task]] = {
    for {
      configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
      executorModule <- ExecutorModule.makeFromExecutionContext[Task](runtime.Platform.executor.asEC)
      clock = Clock.create[Task]
      currentTime <- Resource.liftF(clock.realTime(TimeUnit.MILLISECONDS))
      console <- Resource.pure[Task, Console[Task]](ConsoleModule.make[Task])
      _ <- Resource.liftF(
            console.printLine(s"The current Unix epoch time is $currentTime. This system has ${executorModule.numOfCpus} CPUs.")
          )
      meterRegistry <- MicrometerJmxModule.make[Task](configuration.jmx)
      _ <- Resource.liftF(MicrometerJvmModule.make[Task](meterRegistry))
      serverMetricsModule <- Resource.liftF(MicrometerHttp4sServerMetricsModule.make[Task](meterRegistry, clock))
      routingModule = new Http4sRoutingModule(serverMetricsModule)
      server <- Http4sBlazeServerModule.make[Task](configuration.server, routingModule.router, executorModule.executionContext)
    } yield server
  }

}
