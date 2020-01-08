package com.avast.sst.http4s.server.middleware

import java.net.InetSocketAddress

import cats.effect.{ContextShift, IO, Resource, Timer}
import com.avast.sst.http4s.server.Http4sRouting
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.util.CaseInsensitiveString
import org.http4s.{Header, HttpRoutes, Request, Uri}
import org.scalatest.funsuite.AsyncFunSuite

import scala.concurrent.ExecutionContext

@SuppressWarnings(Array("scalafix:Disable.toString", "scalafix:Disable.get"))
class CorrelationIdMiddlewareTest extends AsyncFunSuite with Http4sDsl[IO] {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit private val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  test("CorrelationIdMiddleware fills Request attributes and HTTP response header") {
    val test = for {
      middleware <- Resource.liftF(CorrelationIdMiddleware.default[IO])
      routes = Http4sRouting.make {
        middleware.wrap {
          HttpRoutes.of[IO] {
            case req @ GET -> Root / "test" =>
              val id = middleware.retrieveCorrelationId(req)
              Ok("test").map(_.withHeaders(Header("Attribute-Value", id.toString)))
          }
        }
      }
      server <- BlazeServerBuilder[IO]
                 .bindSocketAddress(InetSocketAddress.createUnresolved("127.0.0.1", 0))
                 .withExecutionContext(ExecutionContext.global)
                 .withHttpApp(routes)
                 .resource
      client <- BlazeClientBuilder[IO](ExecutionContext.global).resource
    } yield (server, client)

    test
      .use {
        case (server, client) =>
          client
            .fetch(
              Request[IO](uri = Uri.unsafeFromString(s"http://${server.address.getHostString}:${server.address.getPort}/test"))
                .withHeaders(Header("Correlation-Id", "test-value"))
            ) { response =>
              IO.delay {
                assert(response.headers.get(CaseInsensitiveString("Correlation-Id")).get.value === "test-value")
                assert(response.headers.get(CaseInsensitiveString("Attribute-Value")).get.value === "Some(CorrelationId(test-value))")
              }
            }
      }
      .unsafeToFuture()
  }

}
