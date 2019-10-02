package com.avast.server.toolkit.exampleAkka

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import cats.effect.{Clock, Resource}
import com.avast.server.toolkit.akkahttp.AkkaHttpServerModule
import com.avast.server.toolkit.exampleAkka.config.Configuration
import com.avast.server.toolkit.exampleAkka.handlers.{Info, Status}
import com.avast.server.toolkit.execution.ExecutorModule
import com.avast.server.toolkit.pureconfig.PureConfigModule
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import zio.interop.catz._
import zio.{Task, ZIO}

import scala.concurrent.ExecutionContext

object Main extends CatsApp with Directives {

  private val logger = LoggerFactory.getLogger("Main")

  def program: Resource[Task, Unit] = {
    implicit val ec: ExecutionContext = ExecutionContext.global
    implicit val as: ActorSystem = ActorSystem.create("akka-http-system", ConfigFactory.load())
    for {
      configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
      executorModule <- ExecutorModule.makeFromExecutionContext[Task](runtime.Platform.executor.asEC)
      clock = Clock.create[Task]
      status = new Status()
      info = new Info(clock, executorModule)
      http <- AkkaHttpServerModule.make[Task](configuration.akka.http, status.route ~ info.route)
    } yield ()
  }

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = {
    program
      .use(_ => Task.never)
      .fold(
        err => { logger.error(s"Unhandled fatal error ${err.getMessage}", err); 1 },
        _ => 0
      )
  }

}
