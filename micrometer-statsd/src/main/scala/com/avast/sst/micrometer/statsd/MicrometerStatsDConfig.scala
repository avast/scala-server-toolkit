package com.avast.sst.micrometer.statsd

import java.util.concurrent.TimeUnit

import io.micrometer.statsd.{StatsdFlavor, StatsdProtocol}

import scala.concurrent.duration.Duration

final case class MicrometerStatsDConfig(host: String,
                                        port: Int = 8125,
                                        flavor: StatsdFlavor = StatsdFlavor.ETSY,
                                        enabled: Boolean = true,
                                        protocol: StatsdProtocol = StatsdProtocol.UDP,
                                        maxPacketLength: Int = 1400,
                                        pollingFrequency: Duration = Duration(10, TimeUnit.SECONDS),
                                        step: Duration = Duration(1, TimeUnit.MINUTES),
                                        publishUnchangedMeters: Boolean = true,
                                        buffered: Boolean = true)
