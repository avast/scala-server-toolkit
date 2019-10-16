package com.avast.sst.micrometer.statsd

import java.util.concurrent.TimeUnit

import com.avast.sst.micrometer.statsd.MicrometerStatsDConfig.{Flavor, Protocol}

import scala.concurrent.duration.Duration

final case class MicrometerStatsDConfig(host: String,
                                        port: Int = 8125,
                                        flavor: Flavor = Flavor.Etsy,
                                        enabled: Boolean = true,
                                        protocol: Protocol = Protocol.Udp,
                                        maxPacketLength: Int = 1400,
                                        pollingFrequency: Duration = Duration(10, TimeUnit.SECONDS),
                                        step: Duration = Duration(1, TimeUnit.MINUTES),
                                        publishUnchangedMeters: Boolean = true,
                                        buffered: Boolean = true)

object MicrometerStatsDConfig {

  sealed trait Flavor

  object Flavor {

    case object Etsy extends Flavor
    case object Datadog extends Flavor
    case object Telegraf extends Flavor
    case object Sysdig extends Flavor

  }

  sealed trait Protocol

  object Protocol {

    case object Udp extends Protocol
    case object Tcp extends Protocol

  }

}
