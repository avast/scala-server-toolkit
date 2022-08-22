package com.avast.sst.bundle

import cats.effect.Resource
import org.http4s.server.Server
import org.slf4j.LoggerFactory
import zio.*
import zio.interop.catz.*

import scala.annotation.nowarn

/** Extend this `trait` if you want to implement server application using [[zio.ZIO]] effect data type.
  *
  * Implement method `program` with initialization and business logic of your application. It will be automatically run until JVM is shut
  * down in which case all the resources are cleaned up because the whole `program` is a [[cats.effect.Resource]].
  */
trait ZioServerApp extends CatsApp {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def program: Resource[Task, Server]

  @nowarn("msg=dead code")
  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = {
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
          ExitCode.failure
        },
        _ => ExitCode.success
      )
  }

}
