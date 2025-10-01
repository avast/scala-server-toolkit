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

    val test = for {
      client <- Http4sEmberClientModule.make[IO](
        Http4sEmberClientConfig(
          userAgent = `User-Agent`(ProductId("http4s-client", Some("1.2.3")), List(ProductComment("Test")))
        ),
        Some(Blocker.liftExecutionContext(ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())))
      )
      response <- Resource.eval(client.expect[String]("https://ip-info.ff.avast.com/v1/info"))
    } yield assert(response.head === '{')

    test.use(IO.pure).unsafeToFuture()
  }

}
