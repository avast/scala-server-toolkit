package com.avast.sst.http4s.server.middleware

import cats.effect.{ContextShift, IO, Resource, Timer}
import com.avast.sst.http4s.server.Http4sRouting
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.{Header, HttpRoutes, Request, Uri}
import org.scalatest.funsuite.AsyncFunSuite
import org.typelevel.ci.CIString

import java.net.InetSocketAddress
import scala.concurrent.ExecutionContext

@SuppressWarnings(Array("scalafix:Disable.get", "scalafix:Disable.toString", "scalafix:Disable.createUnresolved"))
class CorrelationIdMiddlewareTest extends AsyncFunSuite with Http4sDsl[IO] {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit private val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  test("CorrelationIdMiddleware fills Request attributes and HTTP response header") {
    val test = for {
      middleware <- Resource.eval(CorrelationIdMiddleware.default[IO])
      routes = Http4sRouting.make {
        middleware.wrap {
          HttpRoutes.of[IO] { case req @ GET -> Root / "test" =>
            val id = middleware.retrieveCorrelationId(req)
            Ok("test").map(_.withHeaders(Header.Raw(CIString("Attribute-Value"), id.toString)))
          }
        }
      }
      server <- BlazeServerBuilder[IO](ExecutionContext.global)
        .bindSocketAddress(InetSocketAddress.createUnresolved("127.0.0.1", 0))
        .withHttpApp(routes)
        .resource
      client <- BlazeClientBuilder[IO](ExecutionContext.global).resource
    } yield (server, client)

    test
      .use { case (server, client) =>
        client
          .run(
            Request[IO](uri = Uri.unsafeFromString(s"http://${server.address.getHostString}:${server.address.getPort}/test"))
              .withHeaders(Header.Raw(CIString("Correlation-Id"), "test-value"))
          )
          .use { response =>
            IO.delay {
              assert(response.headers.get(CIString("Correlation-Id")).get.head.value === "test-value")
              assert(response.headers.get(CIString("Attribute-Value")).get.head.value === "Some(CorrelationId(test-value))")
            }
          }
      }
      .unsafeToFuture()
  }

}
