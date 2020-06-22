package com.avast.sst.datastax

import com.datastax.oss.driver.api.core.config.{DriverOption, ProgrammaticDriverConfigLoaderBuilder => DriverBuilder}

import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters._

/** Helper functions to construct Datastax session using Java builder. */
private[datastax] object DatastaxHelper {
  def stringProperty(opt: DriverOption)(value: String)(b: DriverBuilder): DriverBuilder = b.withString(opt, value)
  def intProperty(opt: DriverOption)(value: Int)(b: DriverBuilder): DriverBuilder = b.withInt(opt, value)
  def booleanProperty(opt: DriverOption)(value: Boolean)(b: DriverBuilder): DriverBuilder = b.withBoolean(opt, value)
  def durationProperty(opt: DriverOption)(value: Duration)(b: DriverBuilder): DriverBuilder =
    b.withDuration(opt, java.time.Duration.ofNanos(value.toNanos))
  def stringListProperty(opt: DriverOption)(value: List[String])(b: DriverBuilder): DriverBuilder = b.withStringList(opt, value.asJava)
  def intListProperty(opt: DriverOption)(value: List[Int])(b: DriverBuilder): DriverBuilder =
    b.withIntList(opt, value.map(Integer.valueOf).asJava)
  def optional[T](f: T => DriverBuilder => DriverBuilder, value: Option[T])(b: DriverBuilder): DriverBuilder =
    value.map(f(_)(b)).getOrElse(b)
}
