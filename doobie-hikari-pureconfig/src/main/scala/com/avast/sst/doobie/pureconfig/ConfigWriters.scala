package com.avast.sst.doobie.pureconfig

import com.avast.sst.doobie.DoobieHikariConfig
import doobie.enumerated.TransactionIsolation
import pureconfig.ConfigWriter
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveWriter

trait ConfigWriters {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val doobieTransactionIsolationWriter: ConfigWriter[TransactionIsolation] = ConfigWriter[String].contramap[TransactionIsolation] {
    case TransactionIsolation.TransactionNone            => "TRANSACTION_NONE"
    case TransactionIsolation.TransactionReadUncommitted => "TRANSACTION_READ_UNCOMMITTED"
    case TransactionIsolation.TransactionReadCommitted   => "TRANSACTION_READ_COMMITTED"
    case TransactionIsolation.TransactionRepeatableRead  => "TRANSACTION_REPEATABLE_READ"
    case TransactionIsolation.TransactionSerializable    => "TRANSACTION_SERIALIZABLE"
  }

  implicit val doobieDoobieHikariConfigWriter: ConfigWriter[DoobieHikariConfig] = deriveWriter

}
