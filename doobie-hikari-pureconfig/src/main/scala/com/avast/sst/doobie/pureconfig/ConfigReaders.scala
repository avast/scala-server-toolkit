package com.avast.sst.doobie.pureconfig

import cats.syntax.either._
import com.avast.sst.doobie.DoobieHikariConfig
import doobie.enum.TransactionIsolation
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit val transactionIsolationReader: ConfigReader[TransactionIsolation] = ConfigReader[String].emap {
    case "TRANSACTION_NONE"             => TransactionIsolation.TransactionNone.asRight
    case "TRANSACTION_READ_UNCOMMITTED" => TransactionIsolation.TransactionReadUncommitted.asRight
    case "TRANSACTION_READ_COMMITTED"   => TransactionIsolation.TransactionReadCommitted.asRight
    case "TRANSACTION_REPEATABLE_READ"  => TransactionIsolation.TransactionRepeatableRead.asRight
    case "TRANSACTION_SERIALIZABLE"     => TransactionIsolation.TransactionSerializable.asRight
    case unknown                        => Left(CannotConvert(unknown, "TransactionIsolation", "unknown value"))
  }

  implicit val configReader: ConfigReader[DoobieHikariConfig] = deriveReader

}
