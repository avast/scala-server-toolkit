package com.avast.sst.bundle

import cats.effect.Resource
import org.slf4j.LoggerFactory
import zio.*
import zio.interop.catz.*

/** Extend this `trait` if you want to implement application using [[zio.ZIO]] effect data type.
  *
  * Implement method `program` with initialization and business logic of your application. It will be automatically run and cleaned up.
  */
trait ZioResourceApp[A] extends CatsApp {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def program: Resource[Task, A]

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = {
    program
      .use(_ => Task.unit)
      .fold(
        ex => {
          logger.error("Application initialization failed!", ex)
          ExitCode.failure
        },
        _ => ExitCode.success
      )
  }

}
