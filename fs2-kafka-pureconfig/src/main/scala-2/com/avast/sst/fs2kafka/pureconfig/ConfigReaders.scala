package com.avast.sst.fs2kafka.pureconfig

import cats.syntax.either._
import com.avast.sst.fs2kafka.{ConsumerConfig, ProducerConfig}
import fs2.kafka.{Acks, AutoOffsetReset, CommitRecovery, IsolationLevel}
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto._

@SuppressWarnings(Array("scalafix:DisableSyntax.=="))
trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val fs2KafkaCommitRecoveryConfigReader: ConfigReader[CommitRecovery] = ConfigReader[String].emap {
    case s if s.toLowerCase() == "default" => CommitRecovery.Default.asRight
    case s if s.toLowerCase() == "none"    => CommitRecovery.None.asRight
    case value                             => CannotConvert(value, "CommitRecovery", "default|none").asLeft
  }

  implicit val fs2KafkaAutoOffsetResetConfigReader: ConfigReader[AutoOffsetReset] = ConfigReader[String].emap {
    case s if s.toLowerCase() == "earliest" => AutoOffsetReset.Earliest.asRight
    case s if s.toLowerCase() == "latest"   => AutoOffsetReset.Latest.asRight
    case s if s.toLowerCase() == "none"     => AutoOffsetReset.None.asRight
    case value                              => CannotConvert(value, "AutoOffsetReset", "earliest|latest|none").asLeft
  }

  implicit val fs2KafkaIsolationLevelConfigReader: ConfigReader[IsolationLevel] = ConfigReader[String].emap {
    case s if s.toLowerCase() == "read_committed"   => IsolationLevel.ReadCommitted.asRight
    case s if s.toLowerCase() == "read_uncommitted" => IsolationLevel.ReadUncommitted.asRight
    case value                                      => CannotConvert(value, "IsolationLevel", "read_committed|read_uncommitted").asLeft
  }

  implicit val fs2KafkaAcksConfigReader: ConfigReader[Acks] = ConfigReader[String].emap {
    case s if s.toLowerCase() == "0"   => Acks.Zero.asRight
    case s if s.toLowerCase() == "1"   => Acks.One.asRight
    case s if s.toLowerCase() == "all" => Acks.All.asRight
    case value                         => CannotConvert(value, "Acks", "0|1|all").asLeft
  }

  implicit val fs2KafkaConsumerConfigReader: ConfigReader[ConsumerConfig] = deriveReader[ConsumerConfig]

  implicit val fs2KafkaProducerConfigReader: ConfigReader[ProducerConfig] = deriveReader[ProducerConfig]

}
