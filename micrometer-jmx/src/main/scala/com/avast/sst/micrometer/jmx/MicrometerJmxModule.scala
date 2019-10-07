package com.avast.sst.micrometer.jmx

import cats.effect.{Resource, Sync}
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.jmx.JmxReporter
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.config.NamingConvention
import io.micrometer.core.instrument.util.HierarchicalNameMapper
import io.micrometer.jmx.{JmxConfig, JmxMeterRegistry}

import scala.language.higherKinds

object MicrometerJmxModule {

  def make[F[_]: Sync](config: MicrometerJmxConfig): Resource[F, JmxMeterRegistry] = {
    Resource
      .make {
        Sync[F].delay {
          if (config.enableTypeScopeNameHierarchy) {
            val dropwizardRegistry = new MetricRegistry
            val registry = new JmxMeterRegistry(
              new DomainJmxConfig(config.domain),
              Clock.SYSTEM,
              HierarchicalNameMapper.DEFAULT,
              dropwizardRegistry,
              makeJmxReporter(dropwizardRegistry, config.domain)
            )
            registry.config().namingConvention(NamingConvention.dot)
            registry
          } else {
            new JmxMeterRegistry(new DomainJmxConfig(config.domain), Clock.SYSTEM)
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

  private class DomainJmxConfig(domain: String) extends JmxConfig {
    override def get(key: String): String = null
    override def domain(): String = domain
  }

}
