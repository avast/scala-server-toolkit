package com.avast.sst.example.config

import com.avast.sst.datastax.config.DatastaxDriverConfig
import com.avast.sst.datastax.pureconfig.implicits._
import com.avast.sst.doobie.DoobieHikariConfig
import com.avast.sst.doobie.pureconfig.implicits._
import com.avast.sst.http4s.client.Http4sBlazeClientConfig
import com.avast.sst.http4s.client.pureconfig.implicits._
import com.avast.sst.http4s.server.Http4sBlazeServerConfig
import com.avast.sst.http4s.server.pureconfig.implicits._
import com.avast.sst.jvm.execution.ThreadPoolExecutorConfig
import com.avast.sst.jvm.pureconfig.implicits._
import com.avast.sst.micrometer.jmx.MicrometerJmxConfig
import com.avast.sst.micrometer.jmx.pureconfig.implicits._
import com.avast.sst.monix.catnap.CircuitBreakerConfig
import com.avast.sst.monix.catnap.pureconfig.implicits._
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

final case class Configuration(server: Http4sBlazeServerConfig,
                               database: DoobieHikariConfig,
                               boundedConnectExecutor: ThreadPoolExecutorConfig,
                               client: Http4sBlazeClientConfig,
                               circuitBreaker: CircuitBreakerConfig,
                               jmx: MicrometerJmxConfig,
                               datastaxDriver: DatastaxDriverConfig)

object Configuration {

  implicit val reader: ConfigReader[Configuration] = deriveReader

}
