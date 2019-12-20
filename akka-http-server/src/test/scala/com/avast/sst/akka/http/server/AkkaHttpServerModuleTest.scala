package com.avast.sst.akka.http.server

import akka.http.scaladsl.server.{Directives, Route}
import cats.effect.{ContextShift, IO}
import com.avast.sst.akka.http.server.config.AkkaHttpServerConfig
import com.avast.sst.http4s.client.{Http4sBlazeClientConfig, Http4sBlazeClientModule}
import com.typesafe.config.ConfigFactory
import org.scalatest.funsuite.AsyncFunSuite

import scala.concurrent.ExecutionContext

class AkkaHttpServerModuleTest extends AsyncFunSuite with Directives {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  test("Simple HTTP server") {
    val routes: Route =
      pathPrefix("test") {
        pathEndOrSingleSlash {
          get {
            complete("test")
          }
        }
      }

    val test = for {
      actorSystem <- AkkaHttpActorSystemModule.make[IO](ConfigFactory.empty())
      server <- AkkaHttpServerModule.makeOrRaise[IO](AkkaHttpServerConfig("127.0.0.1", 0), None, routes, ExecutionContext.global, actorSystem)
      client <- Http4sBlazeClientModule.make[IO](Http4sBlazeClientConfig(), ExecutionContext.global)
    } yield (server, client)

    test
      .use {
        case (server, client) =>
          client
            .expect[String](s"http://${server.localAddress.getHostString}:${server.localAddress.getPort}/test")
            .map(response => assert(response === "test"))
      }
      .unsafeToFuture()
  }

}
