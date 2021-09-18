package com.avast.sst.http4s.server.micrometer

import cats.effect.IO
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.http4s.{Method, Status}
import org.scalatest.funsuite.AnyFunSuite

import java.util.concurrent.{Executors, TimeUnit}
import scala.concurrent.ExecutionContext

class MicrometerHttp4sMetricsOpsModuleTest extends AnyFunSuite {

  test("http4s MetricsOps for Micrometer") {
    val registry = new SimpleMeterRegistry()
    val blocker = Blocker.liftExecutionContext(ExecutionContext.fromExecutor(Executors.newCachedThreadPool()))
    val metricsOps = MicrometerHttp4sMetricsOpsModule.make[IO](registry, blocker).unsafeRunSync()

    metricsOps.increaseActiveRequests(None).unsafeRunSync()
    metricsOps.recordTotalTime(Method.GET, Status.Ok, 2500, None).unsafeRunSync()

    assert(registry.get("http.global.active-requests").gauge().value() === 1)
    assert(registry.get("http.global.total-time").timer().count() === 1)
    assert(registry.get("http.global.total-time").timer().totalTime(TimeUnit.NANOSECONDS) > 2499)
    assert(registry.get("http.global.total-time").tags("status", "200").timer().count() === 1)
    assert(registry.get("http.global.total-time").tags("status-class", "2xx").timer().count() === 1)
  }

}
