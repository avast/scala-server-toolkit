package com.avast.sst.fs2kafka

import com.avast.sst.fs2kafka.ConsumerConfig._
import com.github.ghik.silencer.silent
import fs2.kafka.{AutoOffsetReset, CommitRecovery, IsolationLevel}
import org.apache.kafka.clients.consumer.{ConsumerConfig => ApacheConsumerConfig}

import java.util.concurrent.TimeUnit.{MILLISECONDS, SECONDS}
import scala.concurrent.duration.FiniteDuration
import scala.jdk.CollectionConverters._

@silent("dead code")
final case class ConsumerConfig(
    bootstrapServers: List[String],
    groupId: String,
    groupInstanceId: Option[String] = None,
    clientId: Option[String] = None,
    clientRack: Option[String] = None,
    autoOffsetReset: AutoOffsetReset = AutoOffsetReset.None,
    enableAutoCommit: Boolean = false,
    autoCommitInterval: FiniteDuration = defaultMillis(ApacheConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG),
    allowAutoCreateTopics: Boolean = default(ApacheConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG),
    closeTimeout: FiniteDuration = FiniteDuration(20, SECONDS),
    commitRecovery: CommitRecovery = CommitRecovery.Default,
    commitTimeout: FiniteDuration = FiniteDuration(15, SECONDS),
    defaultApiTimeout: FiniteDuration = defaultMillis(ApacheConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG),
    heartbeatInterval: FiniteDuration = defaultMillis(ApacheConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG),
    isolationLevel: IsolationLevel = defaultIsolationLevel,
    maxPrefetchBatches: Int = 2,
    pollInterval: FiniteDuration = FiniteDuration(50, MILLISECONDS),
    pollTimeout: FiniteDuration = FiniteDuration(50, MILLISECONDS),
    maxPollInterval: FiniteDuration = defaultMillis(ApacheConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG),
    maxPollRecords: Int = default(ApacheConsumerConfig.MAX_POLL_RECORDS_CONFIG),
    requestTimeout: FiniteDuration = defaultMillis(ApacheConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG),
    sessionTimeout: FiniteDuration = defaultMillis(ApacheConsumerConfig.SESSION_TIMEOUT_MS_CONFIG),
    properties: Map[String, String] = Map.empty
)

object ConsumerConfig {

  private val officialDefaults = ApacheConsumerConfig.configDef().defaultValues().asScala

  private def default[A](key: String): A = officialDefaults(key).asInstanceOf[A]

  private def defaultMillis(key: String): FiniteDuration = FiniteDuration(default[Int](key).toLong, MILLISECONDS)

  private val defaultIsolationLevel = default[String](ApacheConsumerConfig.ISOLATION_LEVEL_CONFIG) match {
    case "read_uncommitted" => IsolationLevel.ReadUncommitted
    case "read_committed"   => IsolationLevel.ReadCommitted
  }

}
