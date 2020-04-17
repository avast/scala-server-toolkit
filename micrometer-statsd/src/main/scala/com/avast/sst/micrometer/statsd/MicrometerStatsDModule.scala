package com.avast.sst.micrometer.statsd

import java.time.Duration

import cats.effect.{Resource, Sync}
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.util.HierarchicalNameMapper
import io.micrometer.statsd.{StatsdConfig, StatsdFlavor, StatsdMeterRegistry, StatsdProtocol}

object MicrometerStatsDModule {

  /** Makes configured [[io.micrometer.statsd.StatsdMeterRegistry]]. */
  def make[F[_]: Sync](
      config: MicrometerStatsDConfig,
      clock: Clock = Clock.SYSTEM,
      nameMapper: HierarchicalNameMapper = HierarchicalNameMapper.DEFAULT
  ): Resource[F, StatsdMeterRegistry] = {
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

    override val prefix: String = c.prefix

    override val flavor: StatsdFlavor = c.flavor

    override val enabled: Boolean = c.enabled

    override val host: String = c.host

    override val port: Int = c.port

    override val protocol: StatsdProtocol = c.protocol

    override val maxPacketLength: Int = c.maxPacketLength

    override val pollingFrequency: Duration = java.time.Duration.ofMillis(c.pollingFrequency.toMillis)

    override val step: Duration = java.time.Duration.ofMillis(c.step.toMillis)

    override val publishUnchangedMeters: Boolean = c.publishUnchangedMeters

    override val buffered: Boolean = c.buffered

    // the method is @Nullable and we don't need to implement it here
    @SuppressWarnings(Array("scalafix:DisableSyntax.null"))
    override def get(key: String): String = null

  }

}
