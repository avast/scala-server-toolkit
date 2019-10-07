package com.avast.sst.bundle

import cats.effect.Resource
import com.github.ghik.silencer.silent
import org.http4s.server.Server
import org.slf4j.LoggerFactory
import zio.interop.catz._
import zio.{Task, UIO, ZIO}

trait ZioServerApp extends CatsApp {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def program: Resource[Task, Server[Task]]

  @silent("dead code")
  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = {
    program
      .use { server =>
        for {
          _ <- UIO.effectTotal(logger.info(s"Server started @ ${server.address.getHostString}:${server.address.getPort}"))
          _ <- Task.never
        } yield server
      }
      .fold(
        ex => {
          logger.error("Server initialization failed!", ex)
          1
        },
        _ => 0
      )
  }

}
