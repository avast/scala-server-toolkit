package com.avast.server.toolkit.example

object Test extends App {

  import com.avast.server.toolkit.execution.ExecutorModule
  import com.avast.server.toolkit.http4s._
  import com.avast.server.toolkit.system.console.ConsoleModule
  import org.http4s.dsl.Http4sDsl
  import org.http4s.HttpRoutes
  import zio.DefaultRuntime
  import zio.interop.catz._
  import zio.interop.catz.implicits._
  import zio.Task

  implicit val runtime = new DefaultRuntime {} // this is just needed in example

  val dsl = Http4sDsl[Task] // this is just needed in example
  import dsl._

  val routes = Http4sRouting.make {
    HttpRoutes.of[Task] {
      case GET -> Root / "hello" => Ok("Hello World!")
    }
  }

  val resource = for {
    executorModule <- ExecutorModule.makeDefault[Task]
    console = ConsoleModule.make[Task]
    server <- Http4sBlazeServerModule.make[Task](Http4sBlazeServerConfig("127.0.0.1", 0), routes, executorModule.executionContext)
    client <- Http4sBlazeClient.make[Task](Http4sBlazeClientConfig(), executorModule.executionContext)
  } yield (server, client, console)

  val program = resource
    .use {
      case (server, client, console) =>
        client
          .expect[String](s"http://127.0.0.1:${server.address.getPort}/hello")
          .flatMap(console.printLine)
    }

  runtime.unsafeRun(program)

}
