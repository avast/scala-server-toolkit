package com.avast.sst.micrometer.statsd

import cats.effect.{Resource, Sync}
import com.avast.sst.micrometer.PrefixMeterFilter
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.config.{MeterFilter, NamingConvention}
import io.micrometer.core.instrument.util.HierarchicalNameMapper
import io.micrometer.statsd.*

import java.time.Duration

object MicrometerStatsDModule {

  /** Makes configured [[io.micrometer.statsd.StatsdMeterRegistry]]. */
  def make[F[_]: Sync: ContextShift](
      config: MicrometerStatsDConfig,
      clock: Clock = Clock.SYSTEM,
      nameMapper: HierarchicalNameMapper = HierarchicalNameMapper.DEFAULT,
      namingConvention: Option[NamingConvention] = None,
      meterFilter: Option[MeterFilter] = None
  ): Resource[F, StatsdMeterRegistry] = {
    Resource
      .make {
        Sync[F].delay {
          val registry = StatsdMeterRegistry
            .builder(new CustomStatsdConfig(config))
            .clock(clock)
            .nameMapper(nameMapper)
            .build

          namingConvention.foreach(registry.config().namingConvention)

          if (config.prefix.nonEmpty) {
            registry.config().meterFilter(new PrefixMeterFilter(config.prefix))
          }

          meterFilter.foreach(registry.config().meterFilter)

          val preprocessedTags = config.commonTags.foldRight(List.empty[String]) { case (tag, acc) =>
            tag._1 :: tag._2 :: acc
          }
          registry.config().commonTags(preprocessedTags*)

          registry
        }
      }(registry => blocker.delay(registry.close()))
  }

  private class CustomStatsdConfig(c: MicrometerStatsDConfig) extends StatsdConfig {

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
