package com.avast.sst.bundle

import cats.effect.{ExitCode, Resource}
import monix.eval.{Task, TaskApp}
import org.http4s.server.Server
import org.slf4j.LoggerFactory

trait MonixServerApp extends TaskApp {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def program: Resource[Task, Server[Task]]

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
