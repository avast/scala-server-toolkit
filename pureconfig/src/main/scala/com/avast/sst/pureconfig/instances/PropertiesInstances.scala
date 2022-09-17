package com.avast.sst.pureconfig.instances

import com.typesafe.config.Config
import pureconfig.ConfigReader

import java.util.Properties

import scala.jdk.CollectionConverters.*

trait PropertiesInstances {
  implicit val propertiesReader: ConfigReader[Properties] = ConfigReader[Config].map { config =>
    val properties = new Properties()
    config.entrySet().asScala.foreach { entry => properties.put(entry.getKey, entry.getValue.unwrapped()) }
    properties
  }
}

object PropertiesInstances extends PropertiesInstances
