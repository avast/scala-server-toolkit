package com.avast.sst.micrometer

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.scalatest.funsuite.AnyFunSuite

class PrefixMeterFilterTest extends AnyFunSuite {

  test("prefixing") {
    val registry = new SimpleMeterRegistry
    registry.config().meterFilter(new PrefixMeterFilter("this.is.prefix."))
    val counter = registry.counter("test")

    assert(counter.getId.getName === "this.is.prefix.test")
  }

}
