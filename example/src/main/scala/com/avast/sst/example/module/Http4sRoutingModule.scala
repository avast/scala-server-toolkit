package com.avast.sst.example.module

import com.avast.sst.example.service.RandomService
import com.avast.sst.http4s.server.Http4sRouting
import com.avast.sst.http4s.server.micrometer.MicrometerHttp4sServerMetricsModule
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpApp, HttpRoutes}
import zio.Task
import zio.interop.catz._

class Http4sRoutingModule(randomService: RandomService, serverMetricsModule: MicrometerHttp4sServerMetricsModule[Task])
    extends Http4sDsl[Task] {

  import serverMetricsModule._

  private val helloWorldRoute = routeMetrics.wrap("hello")(Ok("Hello World!"))

  private val routes = HttpRoutes.of[Task] {
    case GET -> Root / "hello"  => helloWorldRoute
    case GET -> Root / "random" => randomService.randomNumber.map(_.toString).flatMap(Ok(_))
  }

  val router: HttpApp[Task] = Http4sRouting.make {
    serverMetrics {
      routes
    }
  }

}
