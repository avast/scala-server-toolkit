package com.avast.sst.http4s.server.micrometer

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.http4s.Status
import org.scalatest.funsuite.AnyFunSuite

class HttpStatusMetricsTest extends AnyFunSuite {

  test("HTTP status monitoring") {
    val simpleMeterRegistry = new SimpleMeterRegistry()
    val target = new HttpStatusMetrics("test", simpleMeterRegistry)

    target.recordHttpStatus(Status.Ok)
    target.recordHttpStatus(Status.NoContent)
    target.recordHttpStatus(Status.BadRequest)
    target.recordHttpStatus(Status.ServiceUnavailable)

    assert(simpleMeterRegistry.get("test.status.2xx").counter().count() === 2.0)
    assert(simpleMeterRegistry.get("test.status.200").counter().count() === 1.0)
    assert(simpleMeterRegistry.get("test.status.204").counter().count() === 1.0)
    assert(simpleMeterRegistry.get("test.status.4xx").counter().count() === 1.0)
    assert(simpleMeterRegistry.get("test.status.5xx").counter().count() === 1.0)
  }

}
