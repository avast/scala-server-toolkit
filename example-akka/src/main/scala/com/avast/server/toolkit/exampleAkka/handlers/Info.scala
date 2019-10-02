package com.avast.server.toolkit.exampleAkka.handlers

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{Directives, Route}
import cats.effect.Clock
import com.avast.server.toolkit.execution.ExecutorModule
import zio.{Runtime, Task}

import scala.concurrent.Future
import scala.language.implicitConversions

class Info(clock: Clock[Task], executorModule: ExecutorModule[Task])(implicit runtime: Runtime[Any])
  extends Directives {

  val route: Route =
    pathPrefix("info") {
      pathEndOrSingleSlash {
        get {
          complete(returnMessage())
        }
      }
    }

  private def returnMessage() = {
    for {
      currentTime <- clock.realTime(TimeUnit.MILLISECONDS)
      numOfCpus = executorModule.numOfCpus
    } yield s"The current Unix epoch time is $currentTime. This system has $numOfCpus CPUs."
  }

  implicit private def toResponseMarshallable(from: Task[String]): ToResponseMarshallable = {
    Future.successful { // or Future.apply to spin up asynchronous computation?
      runtime.unsafeRun(from)
    }
  }

}
