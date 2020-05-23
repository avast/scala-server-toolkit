package com.avast.sst.fs2kafka

import java.util.concurrent.TimeUnit.{MILLISECONDS, SECONDS}

import com.avast.sst.fs2kafka.ProducerConfig._
import fs2.kafka.Acks
import org.apache.kafka.clients.producer.{ProducerConfig => ApacheProducerConfig}

import scala.concurrent.duration.FiniteDuration
import scala.jdk.CollectionConverters._

final case class ProducerConfig(
    bootstrapServers: List[String],
    clientId: Option[String] = None,
    acks: Acks = defaultAcks,
    batchSize: Int = default[Int](ApacheProducerConfig.BATCH_SIZE_CONFIG),
    closeTimeout: FiniteDuration = FiniteDuration(60, SECONDS),
    deliveryTimeout: FiniteDuration = defaultMillis(ApacheProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG),
    requestTimeout: FiniteDuration = defaultMillis(ApacheProducerConfig.REQUEST_TIMEOUT_MS_CONFIG),
    linger: FiniteDuration = defaultMillisLong(ApacheProducerConfig.LINGER_MS_CONFIG),
    enableIdempotence: Boolean = default[Boolean](ApacheProducerConfig.ENABLE_IDEMPOTENCE_CONFIG),
    maxInFlightRequestsPerConnection: Int = default[Int](ApacheProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION),
    parallelism: Int = 100,
    retries: Int = 0
)

object ProducerConfig {

  private val officialDefaults = ApacheProducerConfig.configDef().defaultValues().asScala

  private def default[A](key: String): A = officialDefaults(key).asInstanceOf[A]

  private def defaultMillis(key: String): FiniteDuration = FiniteDuration(default[Int](key).toLong, MILLISECONDS)
  private def defaultMillisLong(key: String): FiniteDuration = FiniteDuration(default[Long](key), MILLISECONDS)

  private val defaultAcks = default[String](ApacheProducerConfig.ACKS_CONFIG) match {
    case "all" => Acks.All
    case "0"   => Acks.Zero
    case "1"   => Acks.One
  }

}
