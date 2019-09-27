package com.avast.server.toolkit.example

import java.util.concurrent.TimeUnit

import cats.effect.{Clock, Resource}
import com.avast.server.toolkit.example.config.Configuration
import com.avast.server.toolkit.execution.ExecutorModule
import com.avast.server.toolkit.pureconfig.PureConfigModule
import com.avast.server.toolkit.system.console.{Console, ConsoleModule}
import zio.interop.catz._
import zio.{Task, ZIO}

object Main extends CatsApp {

  def program: Resource[Task, Unit] = {

    for {
      configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
      executorModule <- ExecutorModule.makeFromExecutionContext[Task](runtime.Platform.executor.asEC)
      clock = Clock.create[Task]
      currentTime <- Resource.liftF(clock.realTime(TimeUnit.MILLISECONDS))
      console <- Resource.pure[Task, Console[Task]](ConsoleModule.make[Task])
      _ <- Resource.liftF(
            console.printLine(s"The current Unix epoch time is $currentTime. This system has ${executorModule.numOfCpus} CPUs.")
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
