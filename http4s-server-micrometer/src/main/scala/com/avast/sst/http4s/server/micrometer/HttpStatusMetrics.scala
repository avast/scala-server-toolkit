package com.avast.sst.http4s.server.micrometer

import io.micrometer.core.instrument.{Counter, MeterRegistry}
import org.http4s.Status

import scala.collection.concurrent.TrieMap

/** Records counts of HTTP statuses in [[io.micrometer.core.instrument.MeterRegistry]]. */
private[micrometer] class HttpStatusMetrics(prefix: String, meterRegistry: MeterRegistry) {

  private val meters = TrieMap[Int, Counter](
    1 -> meterRegistry.counter(s"$prefix.status.1xx"),
    2 -> meterRegistry.counter(s"$prefix.status.2xx"),
    3 -> meterRegistry.counter(s"$prefix.status.3xx"),
    4 -> meterRegistry.counter(s"$prefix.status.4xx"),
    5 -> meterRegistry.counter(s"$prefix.status.5xx")
  )

  def recordHttpStatus(status: Status): Unit = {
    val code = status.code
    meters(code / 100).increment()
    meters.getOrElseUpdate(code, meterRegistry.counter(s"$prefix.status.$code")).increment()
  }

}
