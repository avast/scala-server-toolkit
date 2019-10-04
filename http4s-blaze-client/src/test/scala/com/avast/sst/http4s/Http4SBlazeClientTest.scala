package com.avast.sst.http4s

import cats.effect._
import org.http4s.headers.{`User-Agent`, AgentComment, AgentProduct}
import org.scalatest.AsyncFunSuite

import scala.concurrent.ExecutionContext

class Http4SBlazeClientTest extends AsyncFunSuite {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  test("Initialization of HTTP client and simple GET") {
    val expected = """|{
                      |  "user-agent": "http4s-client/1.2.3 (Test)"
                      |}
                      |""".stripMargin

    val test = for {
      client <- Http4sBlazeClient.make[IO](
                 Http4sBlazeClientConfig(
                   userAgent = `User-Agent`(AgentProduct("http4s-client", Some("1.2.3")), List(AgentComment("Test")))
                 ),
                 ExecutionContext.global
               )
      response <- Resource.liftF(client.expect[String]("https://httpbin.org/user-agent"))
    } yield assert(response === expected)

    test.use(IO.pure).unsafeToFuture()
  }

}
