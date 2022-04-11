package com.avast.sst.ssl

import com.typesafe.sslconfig.util.{LoggerFactory, NoDepsLogger}
import org.slf4j.{Logger, LoggerFactory as Slf4jLoggerFactory}

private class Slf4jLogger(l: Logger) extends NoDepsLogger {

  override def isDebugEnabled: Boolean = l.isDebugEnabled

  override def debug(msg: String): Unit = l.debug(msg)

  override def info(msg: String): Unit = l.info(msg)

  override def warn(msg: String): Unit = l.warn(msg)

  override def error(msg: String): Unit = l.error(msg)

  override def error(msg: String, throwable: Throwable): Unit = l.error(msg, throwable)

}

private[ssl] object Slf4jLogger {

  def factory: LoggerFactory =
    new LoggerFactory {
      override def apply(clazz: Class[?]): NoDepsLogger = new Slf4jLogger(Slf4jLoggerFactory.getLogger(clazz))
      override def apply(name: String): NoDepsLogger = new Slf4jLogger(Slf4jLoggerFactory.getLogger(name))
    }

}
