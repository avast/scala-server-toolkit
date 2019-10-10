package com.avast.sst.http4s.server

import cats.effect.{ContextShift, IO, Timer}
import com.avast.sst.http4s.client.{Http4sBlazeClient, Http4sBlazeClientConfig}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.scalatest.AsyncFunSuite

import scala.concurrent.ExecutionContext

class Http4sBlazeServerModuleTest extends AsyncFunSuite with Http4sDsl[IO] {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit private val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  test("Simple HTTP server") {
    val routes = Http4sRouting.make(HttpRoutes.of[IO] {
      case GET -> Root / "test" => Ok("test")
    })
    val test = for {
      server <- Http4sBlazeServerModule.make[IO](Http4sBlazeServerConfig("127.0.0.1", 0), routes, ExecutionContext.global)
      client <- Http4sBlazeClient.make[IO](Http4sBlazeClientConfig(), ExecutionContext.global)
    } yield (server, client)

    test
      .use {
        case (server, client) =>
          client
            .expect[String](s"http://${server.address.getHostString}:${server.address.getPort}/test")
            .map(response => assert(response === "test"))
      }
      .unsafeToFuture()
  }

}
