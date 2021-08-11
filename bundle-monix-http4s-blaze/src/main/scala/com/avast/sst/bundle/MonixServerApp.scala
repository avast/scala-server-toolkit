package com.avast.sst.bundle

import cats.effect.{ExitCode, Resource}
import monix.eval.{Task, TaskApp}
import org.http4s.server.Server
import org.slf4j.LoggerFactory

/** Extend this `trait` if you want to implement server application using [[monix.eval.Task]] effect data type.
  *
  * Implement method `program` with initialization and business logic of your application. It will be automatically run until JVM is shut
  * down in which case all the resources are cleaned up because the whole `program` is a [[cats.effect.Resource]].
  */
trait MonixServerApp extends TaskApp {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def program: Resource[Task, Server]

  override def run(args: List[String]): Task[ExitCode] = {
    program
      .use { server =>
        for {
          _ <- Task.delay(logger.info(s"Server started @ ${server.address.getHostString}:${server.address.getPort}"))
          _ <- Task.never[Unit]
        } yield server
      }
      .redeem(
        ex => {
          logger.error("Server initialization failed!", ex)
          ExitCode.Error
        },
        _ => ExitCode.Success
      )
  }

}
