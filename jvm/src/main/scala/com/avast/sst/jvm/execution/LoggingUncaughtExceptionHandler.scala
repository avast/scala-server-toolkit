package com.avast.sst.jvm.execution

import java.lang.Thread.UncaughtExceptionHandler

import org.slf4j.LoggerFactory

object LoggingUncaughtExceptionHandler extends UncaughtExceptionHandler {

  private lazy val logger = LoggerFactory.getLogger("LoggingUncaughtExceptionHandler")

  override def uncaughtException(t: Thread, ex: Throwable): Unit = logger.error(s"Uncaught exception on thread ${t.getName}", ex)

}
