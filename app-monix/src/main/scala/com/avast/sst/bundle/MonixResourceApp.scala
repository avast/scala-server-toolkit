package com.avast.sst.bundle

import cats.effect.{ExitCode, Resource}
import monix.eval.{Task, TaskApp}
import org.slf4j.LoggerFactory

/** Extend this `trait` if you want to implement application using [[monix.eval.Task]] effect data type.
  *
  * Implement method `program` with initialization and business logic of your application. It will be automatically run and cleaned up.
  */
trait MonixResourceApp[A] extends TaskApp {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def program: Resource[Task, A]

  override def run(args: List[String]): Task[ExitCode] = {
    program
      .use(_ => Task.unit)
      .redeem(
        ex => {
          logger.error("Application initialization failed!", ex)
          ExitCode.Error
        },
        _ => ExitCode.Success
      )
  }

}
