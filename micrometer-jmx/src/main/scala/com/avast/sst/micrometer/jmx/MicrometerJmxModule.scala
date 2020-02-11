package com.avast.sst.micrometer.jmx

import java.time.Duration

import cats.effect.{Resource, Sync}
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.jmx.JmxReporter
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.config.NamingConvention
import io.micrometer.core.instrument.util.HierarchicalNameMapper
import io.micrometer.jmx.{JmxConfig, JmxMeterRegistry}

object MicrometerJmxModule {

  /** Makes configured [[io.micrometer.jmx.JmxMeterRegistry]]. */
  def make[F[_]: Sync](config: MicrometerJmxConfig,
                       clock: Clock = Clock.SYSTEM,
                       nameMapper: HierarchicalNameMapper = HierarchicalNameMapper.DEFAULT): Resource[F, JmxMeterRegistry] = {
    Resource
      .make {
        Sync[F].delay {
          if (config.enableTypeScopeNameHierarchy) {
            val dropwizardRegistry = new MetricRegistry
            val registry = new JmxMeterRegistry(
              new CustomJmxConfig(config),
              clock,
              nameMapper,
              dropwizardRegistry,
              makeJmxReporter(dropwizardRegistry, config.domain)
            )
            registry.config.namingConvention(NamingConvention.dot)
            registry
          } else {
            new JmxMeterRegistry(new CustomJmxConfig(config), clock, nameMapper)
          }
        }
      }(registry => Sync[F].delay(registry.close()))
  }

  private def makeJmxReporter(metricRegistry: MetricRegistry, domain: String) = {
    JmxReporter
      .forRegistry(metricRegistry)
      .inDomain(domain)
      .createsObjectNamesWith(new TypeScopeNameObjectNameFactory())
      .build
  }

  private class CustomJmxConfig(c: MicrometerJmxConfig) extends JmxConfig {

    override val domain: String = c.domain
    override val step: Duration = Duration.ofMillis(c.step.toMillis)

    // the method is @Nullable and we don't need to implement it here
    @SuppressWarnings(Array("scalafix:DisableSyntax.null"))
    override def get(key: String): String = null

  }

}
