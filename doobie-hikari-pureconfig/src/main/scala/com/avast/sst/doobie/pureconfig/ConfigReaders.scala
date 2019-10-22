package com.avast.sst.doobie.pureconfig

import com.avast.sst.doobie.DoobieHikariConfig
import doobie.enum.TransactionIsolation
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}

trait ConfigReaders {

  implicit val transactionIsolationReader: ConfigReader[TransactionIsolation] = deriveEnumerationReader

  implicit val configReader: ConfigReader[DoobieHikariConfig] = deriveReader

}
