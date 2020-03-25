package com.avast.sst.grpc.server.micrometer

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import io.grpc._
import io.micrometer.core.instrument.{MeterRegistry, Timer}

/** Records important gRPC call metrics in [[io.micrometer.core.instrument.MeterRegistry]].
  *
  * Metrics:
  *   - grpc.<method>.current-calls
  *   - grpc.<method>.successes
  *   - grpc.<method>.failures
  */
class MonitoringServerInterceptor(meterRegistry: MeterRegistry) extends ServerInterceptor {

  private val gaugeCache = new ConcurrentHashMap[String, AtomicLong]()
  private val timerCache = new ConcurrentHashMap[String, Timer]()

  override def interceptCall[ReqT, RespT](
      call: ServerCall[ReqT, RespT],
      headers: Metadata,
      next: ServerCallHandler[ReqT, RespT]
  ): ServerCall.Listener[ReqT] = {
    val prefix = s"grpc.${call.getMethodDescriptor.getFullMethodName}"
    val currentCallsCounter = makeGauge(s"$prefix.current-calls")
    currentCallsCounter.incrementAndGet()
    val start = System.nanoTime
    val newCall = new CloseServerCall(prefix, start, currentCallsCounter, call)
    next.startCall(newCall, headers)
  }

  private class CloseServerCall[A, B](prefix: String, start: Long, currentCallsCounter: AtomicLong, delegate: ServerCall[A, B])
      extends SimpleForwardingServerCall[A, B](delegate) {
    override def close(status: Status, trailers: Metadata): Unit = {
      val durationNs = System.nanoTime - start
      if (status.isOk) {
        makeTimer(s"$prefix.successes").record(durationNs, TimeUnit.NANOSECONDS)
      } else {
        makeTimer(s"$prefix.failures").record(durationNs, TimeUnit.NANOSECONDS)
      }
      currentCallsCounter.decrementAndGet()
      super.close(status, trailers)
    }
  }

  private def makeGauge(name: String): AtomicLong = {
    gaugeCache.computeIfAbsent(name, n => {
      val counter = new AtomicLong()
      meterRegistry.gauge(n, counter)
      counter
    })
  }

  private def makeTimer(name: String): Timer = timerCache.computeIfAbsent(name, meterRegistry.timer(_))

}
