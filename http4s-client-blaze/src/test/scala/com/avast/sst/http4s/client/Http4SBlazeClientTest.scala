package com.avast.sst.http4s.client

import cats.effect.*
import org.http4s.headers.*
import org.http4s.{ProductComment, ProductId}
import org.scalatest.funsuite.AsyncFunSuite

import scala.concurrent.ExecutionContext

class Http4SBlazeClientTest extends AsyncFunSuite {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  test("Initialization of HTTP client and simple GET") {

    val test = for {
      client <- Http4sBlazeClientModule.make[IO](
        Http4sBlazeClientConfig(
          userAgent = `User-Agent`(ProductId("http4s-client", Some("1.2.3")), List(ProductComment("Test")))
        ),
        ExecutionContext.global
      )
      response <- Resource.eval(client.expect[String]("https://ip-info.ff.avast.com/v1/info"))
    } yield assert(response.head === '{')

    test.use(IO.pure).unsafeToFuture()
  }

}
