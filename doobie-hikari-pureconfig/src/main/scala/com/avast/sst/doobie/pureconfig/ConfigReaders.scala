package com.avast.sst.doobie.pureconfig

import cats.syntax.either._
import com.avast.sst.doobie.DoobieHikariConfig
import doobie.enumerated.TransactionIsolation
import pureconfig.{ConfigReader, ConfigWriter}
import pureconfig.error.CannotConvert
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.{deriveReader, deriveWriter}

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val doobieTransactionIsolationReader: ConfigReader[TransactionIsolation] = ConfigReader[String].emap {
    case "TRANSACTION_NONE"             => TransactionIsolation.TransactionNone.asRight
    case "TRANSACTION_READ_UNCOMMITTED" => TransactionIsolation.TransactionReadUncommitted.asRight
    case "TRANSACTION_READ_COMMITTED"   => TransactionIsolation.TransactionReadCommitted.asRight
    case "TRANSACTION_REPEATABLE_READ"  => TransactionIsolation.TransactionRepeatableRead.asRight
    case "TRANSACTION_SERIALIZABLE"     => TransactionIsolation.TransactionSerializable.asRight
    case unknown                        => Left(CannotConvert(unknown, "TransactionIsolation", "unknown value"))
  }

  implicit val doobieTransactionIsolationWriter: ConfigWriter[TransactionIsolation] = ConfigWriter[String].contramap[TransactionIsolation] {
    case TransactionIsolation.TransactionNone            => "TRANSACTION_NONE"
    case TransactionIsolation.TransactionReadUncommitted => "TRANSACTION_READ_UNCOMMITTED"
    case TransactionIsolation.TransactionReadCommitted   => "TRANSACTION_READ_COMMITTED"
    case TransactionIsolation.TransactionRepeatableRead  => "TRANSACTION_REPEATABLE_READ"
    case TransactionIsolation.TransactionSerializable    => "TRANSACTION_SERIALIZABLE"
  }

  implicit val doobieDoobieHikariConfigReader: ConfigReader[DoobieHikariConfig] = deriveReader
  implicit val doobieDoobieHikariConfigWriter: ConfigWriter[DoobieHikariConfig] = deriveWriter

}
