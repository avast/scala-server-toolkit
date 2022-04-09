package com.avast.sst.http4s.server.micrometer

import cats.effect.SyncIO
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.http4s.Response
import org.scalatest.funsuite.AnyFunSuite

import scala.language.adhocExtensions

import java.util.concurrent.TimeUnit

class RouteMetricsTest extends AnyFunSuite {

  test("Single route metrics") {
    val registry = new SimpleMeterRegistry()
    val target = new RouteMetrics[SyncIO](registry)

    target.wrap("test")(SyncIO.pure(Response.notFound[SyncIO])).unsafeRunSync()
    assert(registry.get("http.test").timer().count() === 1)
    assert(registry.get("http.test").timer().totalTime(TimeUnit.MILLISECONDS) > 0)
    assert(registry.get("http.test").tags("status", "404").timer().count() === 1)
  }

}
