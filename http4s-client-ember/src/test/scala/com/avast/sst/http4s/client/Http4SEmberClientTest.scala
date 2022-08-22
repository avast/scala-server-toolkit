package com.avast.sst.http4s.client

import cats.effect.*
import org.http4s.headers.*
import org.http4s.{ProductComment, ProductId}
import org.scalatest.funsuite.AsyncFunSuite

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class Http4SEmberClientTest extends AsyncFunSuite {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  implicit private val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  test("Initialization of HTTP client and simple GET") {
    val expected = """|{
                      |  "user-agent": "http4s-client/1.2.3 (Test)"
                      |}
                      |""".stripMargin

    val test = for {
      client <- Http4sEmberClientModule.make[IO](
        Http4sEmberClientConfig(
          userAgent = `User-Agent`(ProductId("http4s-client", Some("1.2.3")), List(ProductComment("Test")))
        ),
        Some(Blocker.liftExecutionContext(ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())))
      )
      response <- Resource.eval(client.expect[String]("https://httpbin.org/user-agent"))
    } yield assert(response === expected)

    test.use(IO.pure).unsafeToFuture()
  }

}
