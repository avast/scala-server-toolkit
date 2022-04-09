package com.avast.sst.micrometer.prometheus

import cats.effect.{Blocker, ContextShift, Resource, Sync}
import com.avast.sst.micrometer.PrefixMeterFilter
import io.micrometer.core.instrument.config.{MeterFilter, NamingConvention}
import io.micrometer.prometheus.{HistogramFlavor, PrometheusConfig, PrometheusMeterRegistry}

import java.time.Duration

object MicrometerPrometheusModule {

  /** Makes configured [[io.micrometer.prometheus.PrometheusMeterRegistry]]. */
  def make[F[_]: Sync: ContextShift](
      config: MicrometerPrometheusConfig,
      blocker: Blocker,
      namingConvention: Option[NamingConvention] = None,
      meterFilter: Option[MeterFilter] = None
  ): Resource[F, PrometheusMeterRegistry] = {
    Resource
      .make {
        Sync[F].delay {
          val registry = new PrometheusMeterRegistry(new CustomPrometheusConfig(config))

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

  private class CustomPrometheusConfig(c: MicrometerPrometheusConfig) extends PrometheusConfig {

    override val step: Duration = java.time.Duration.ofMillis(c.step.toMillis)
    override val prefix: String = c.prefix
    override val descriptions: Boolean = c.descriptions
    override val histogramFlavor: HistogramFlavor = c.histogramFlavor

    // the method is @Nullable and we don't need to implement it here
    @SuppressWarnings(Array("scalafix:DisableSyntax.null"))
    override def get(key: String): String = null

  }

}
