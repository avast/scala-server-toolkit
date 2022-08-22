package com.avast.sst.http4s.server

import cats.effect.{Blocker, ContextShift, IO, Timer}
import com.avast.sst.http4s.client.{Http4sEmberClientConfig, Http4sEmberClientModule}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.scalatest.funsuite.AsyncFunSuite

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class Http4sEmberServerModuleTest extends AsyncFunSuite with Http4sDsl[IO] {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit private val timer: Timer[IO] = IO.timer(ExecutionContext.global)
  private val blocker = Blocker.liftExecutionContext(ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor()))

  test("Simple HTTP server") {
    val routes = Http4sRouting.make(HttpRoutes.of[IO] { case GET -> Root / "test" =>
      Ok("test")
    })
    val test = for {
      server <- Http4sEmberServerModule.make[IO](Http4sEmberServerConfig(), routes, Some(blocker))
      client <- Http4sEmberClientModule.make[IO](Http4sEmberClientConfig(), Some(blocker))
    } yield (server, client)

    test
      .use { case (server, client) =>
        client
          .expect[String](s"http://${server.address.getHostString}:${server.address.getPort}/test")
          .map(response => assert(response === "test"))
      }
      .unsafeToFuture()
  }

}
