package com.avast.sst.http4s.server.micrometer

import java.util.concurrent.TimeUnit

import cats.effect.SyncIO
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.http4s.{Method, Status}
import org.scalatest.FunSuite

class MicrometerHttp4sMetricsOpsModuleTest extends FunSuite {

  test("http4s MetricsOps for Micrometer") {
    val registry = new SimpleMeterRegistry()
    val metricsOps = MicrometerHttp4sMetricsOpsModule.make[SyncIO](registry).unsafeRunSync()

    metricsOps.increaseActiveRequests(None).unsafeRunSync()
    metricsOps.recordTotalTime(Method.GET, Status.Ok, 2500, None).unsafeRunSync()

    assert(registry.get("http.global.active-requests").counter().count() === 1)
    assert(registry.get("http.global.total-time").timer().count() === 1)
    assert(registry.get("http.global.total-time").timer().totalTime(TimeUnit.NANOSECONDS) > 2499)
    assert(registry.get("http.global.status.200").counter().count() === 1)
  }

}
