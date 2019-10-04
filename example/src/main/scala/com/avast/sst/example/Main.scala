package com.avast.sst.example

import java.util.concurrent.TimeUnit

import cats.effect.{Clock, Resource}
import com.avast.sst.example.config.Configuration
import com.avast.sst.execution.ExecutorModule
import com.avast.sst.pureconfig.PureConfigModule
import com.avast.sst.system.console.{Console, ConsoleModule}
import com.github.ghik.silencer.silent
import zio.interop.catz._
import zio.{Task, ZIO}

object Main extends CatsApp {

  def program: Resource[Task, Unit] = {

    for {
      _ <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
      executorModule <- ExecutorModule.makeFromExecutionContext[Task](runtime.Platform.executor.asEC)
      clock = Clock.create[Task]
      currentTime <- Resource.liftF(clock.realTime(TimeUnit.MILLISECONDS))
      console <- Resource.pure[Task, Console[Task]](ConsoleModule.make[Task])
      _ <- Resource.liftF(
            console.printLine(s"The current Unix epoch time is $currentTime. This system has ${executorModule.numOfCpus} CPUs.")
          )
    } yield ()
  }

  @silent("dead code") // false positive
  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = {
    program
      .use(_ => Task.never)
      .fold(
        _ => 1,
        _ => 0
      )
  }

}
