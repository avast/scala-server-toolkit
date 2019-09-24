package com.avast.server.toolkit.example

import java.util.concurrent.TimeUnit

import cats.effect.Resource
import com.avast.server.toolkit.example.config.Configuration
import com.avast.server.toolkit.execution.ExecutorModule
import com.avast.server.toolkit.pureconfig.PureConfigModule
import com.avast.server.toolkit.system.SystemModule
import zio.interop.catz._
import zio.{Task, ZIO}

object Main extends CatsApp {

  def program: Resource[Task, Unit] = {
    for {
      configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
      systemModule <- Resource.liftF(SystemModule.make[Task])
      executorModule <- ExecutorModule.make[Task](runtime.Platform.executor.asEC)
      currentTime <- Resource.liftF(systemModule.clock.realTime(TimeUnit.MILLISECONDS))
      _ <- Resource.liftF(
            systemModule
              .console
              .printLine(s"The current Unix epoch time is $currentTime. This system has ${executorModule.numOfCpus} CPUs.")
          )
    } yield ()
  }

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = {
    program
      .use(_ => Task.never)
      .fold(
        _ => 1,
        _ => 0
      )
  }

}
