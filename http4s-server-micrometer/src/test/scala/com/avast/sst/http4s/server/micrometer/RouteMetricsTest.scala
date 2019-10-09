package com.avast.sst.http4s.server.micrometer

import java.util.concurrent.TimeUnit

import cats.effect.{Clock, SyncIO}
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.http4s.Response
import org.scalatest.FunSuite

class RouteMetricsTest extends FunSuite {

  test("Single route metrics") {
    val registry = new SimpleMeterRegistry()
    val target = new RouteMetrics[SyncIO](registry, Clock.create[SyncIO])

    target.wrap("test")(SyncIO.pure(Response.notFound[SyncIO])).unsafeRunSync()
    assert(registry.get("http.test.active-requests").counter().count() === 0)
    assert(registry.get("http.test.total-time").timer().count() === 1)
    assert(registry.get("http.test.total-time").timer().totalTime(TimeUnit.MILLISECONDS) > 0)
    assert(registry.get("http.test.status.404").counter().count() === 1)
  }

}
