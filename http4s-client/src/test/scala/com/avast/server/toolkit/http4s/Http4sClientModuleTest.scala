package com.avast.server.toolkit.http4s

import cats.effect._
import org.http4s.headers.{`User-Agent`, AgentComment, AgentProduct}
import org.scalatest.AsyncFunSuite

import scala.concurrent.ExecutionContext

class Http4sClientModuleTest extends AsyncFunSuite {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  test("Initialization of HTTP client and simple GET") {
    val expected = """|{
                      |  "user-agent": "http4s-blaze/1.2.3 (Test)"
                      |}
                      |""".stripMargin

    val test = for {
      clientModule <- Http4sClientModule.make[IO](
                       Http4sClientConfig(
                         userAgent = `User-Agent`(AgentProduct("http4s-blaze", Some("1.2.3")), List(AgentComment("Test")))
                       ),
                       ExecutionContext.global
                     )
      client = clientModule.client
      response <- Resource.liftF(client.expect[String]("https://httpbin.org/user-agent"))
    } yield assert(response === expected)

    test.use(IO.pure).unsafeToFuture()
  }

}
