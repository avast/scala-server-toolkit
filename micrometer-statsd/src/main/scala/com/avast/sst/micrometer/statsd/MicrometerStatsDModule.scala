package com.avast.sst.micrometer.statsd

import java.time.Duration

import cats.effect.{Resource, Sync}
import com.avast.sst.micrometer.statsd.MicrometerStatsDConfig.{Flavor, Protocol}
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.util.HierarchicalNameMapper
import io.micrometer.statsd.{StatsdConfig, StatsdFlavor, StatsdMeterRegistry, StatsdProtocol}

import scala.language.higherKinds

object MicrometerStatsDModule {

  /** Makes configured [[io.micrometer.statsd.StatsdMeterRegistry]]. */
  def make[F[_]: Sync](config: MicrometerStatsDConfig,
                       clock: Clock = Clock.SYSTEM,
                       nameMapper: HierarchicalNameMapper = HierarchicalNameMapper.DEFAULT): Resource[F, StatsdMeterRegistry] = {
    Resource
      .make {
        Sync[F].delay {
          StatsdMeterRegistry
            .builder(new CustomStatsdConfig(config))
            .clock(clock)
            .nameMapper(nameMapper)
            .build
        }
      }(registry => Sync[F].delay(registry.close()))
  }

  private class CustomStatsdConfig(c: MicrometerStatsDConfig) extends StatsdConfig {

    override val flavor: StatsdFlavor = c.flavor match {
      case Flavor.Etsy     => StatsdFlavor.ETSY
      case Flavor.Datadog  => StatsdFlavor.DATADOG
      case Flavor.Telegraf => StatsdFlavor.TELEGRAF
      case Flavor.Sysdig   => StatsdFlavor.SYSDIG
    }

    override val enabled: Boolean = c.enabled

    override val host: String = c.host

    override val port: Int = c.port

    override val protocol: StatsdProtocol = c.protocol match {
      case Protocol.Udp => StatsdProtocol.UDP
      case Protocol.Tcp => StatsdProtocol.TCP
    }

    override val maxPacketLength: Int = c.maxPacketLength

    override val pollingFrequency: Duration = java.time.Duration.ofMillis(c.pollingFrequency.toMillis)

    override val step: Duration = java.time.Duration.ofMillis(c.step.toMillis)

    override val publishUnchangedMeters: Boolean = c.publishUnchangedMeters

    override val buffered: Boolean = c.buffered

    // the method is @Nullable and we don't need to implement it here
    @SuppressWarnings(Array("org.wartremover.warts.Null"))
    override def get(key: String): String = null

  }

}
