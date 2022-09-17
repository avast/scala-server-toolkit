package com.avast.sst.pureconfig.instances

import pureconfig.ConfigReader

import java.net.InetAddress
import scala.util.Try

trait InetAddressInstances {
  implicit val inetAddressConfigReader: ConfigReader[InetAddress] =
    ConfigReader.fromNonEmptyStringTry(host => Try { InetAddress.getByName(host) })
}

object InetAddressInstances extends InetAddressInstances
