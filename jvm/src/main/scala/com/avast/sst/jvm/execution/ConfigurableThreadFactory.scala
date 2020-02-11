package com.avast.sst.jvm.execution

import java.lang.Thread.UncaughtExceptionHandler
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{ForkJoinPool, ForkJoinWorkerThread, ThreadFactory}

import com.avast.sst.jvm.execution.ConfigurableThreadFactory.Config

/** Thread factory (both [[java.util.concurrent.ThreadFactory]] and [[java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory]])
  * that creates new threads according to the provided [[com.avast.sst.jvm.execution.ConfigurableThreadFactory.Config]].
  */
class ConfigurableThreadFactory(config: Config) extends ThreadFactory with ForkJoinWorkerThreadFactory {

  private val counter = new AtomicLong(0L)

  override def newThread(r: Runnable): Thread = configure(new Thread(r))

  override def newThread(pool: ForkJoinPool): ForkJoinWorkerThread = {
    configure(ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool))
  }

  private def configure[A <: Thread](thread: A) = {
    config.nameFormat.map(_.format(counter.getAndIncrement())).foreach(thread.setName)
    thread.setDaemon(config.daemon)
    thread.setPriority(config.priority)
    thread.setUncaughtExceptionHandler(config.uncaughtExceptionHandler)
    thread
  }

}

object ConfigurableThreadFactory {

  /**
    * @param nameFormat Formatted with long number, e.g. my-thread-%02d
    */
  final case class Config(
      nameFormat: Option[String] = None,
      daemon: Boolean = false,
      priority: Int = Thread.NORM_PRIORITY,
      uncaughtExceptionHandler: UncaughtExceptionHandler = LoggingUncaughtExceptionHandler
  )

}
