package com.avast.server.toolkit.execution

import java.lang.Thread.UncaughtExceptionHandler

import com.typesafe.scalalogging.LazyLogging

object LoggingUncaughtExceptionHandler extends UncaughtExceptionHandler with LazyLogging {

  override def uncaughtException(t: Thread, ex: Throwable): Unit = logger.error(s"Uncaught exception on thread ${t.getName}", ex)

}
