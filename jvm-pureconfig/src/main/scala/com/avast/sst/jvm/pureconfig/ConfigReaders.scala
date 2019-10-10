package com.avast.sst.jvm.pureconfig

import com.avast.sst.jvm.execution.ForkJoinPoolConfig.TaskPeekingMode
import com.avast.sst.jvm.execution.{ForkJoinPoolConfig, ThreadPoolExecutorConfig}
import com.avast.sst.jvm.ssl.{KeyStoreConfig, KeyStoreType, Protocol, SslContextConfig}
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}

trait ConfigReaders {

  implicit val threadPoolExecutorConfigReader: ConfigReader[ThreadPoolExecutorConfig] = deriveReader

  implicit val taskPeekingModeReader: ConfigReader[TaskPeekingMode] = deriveEnumerationReader

  implicit val forkJoinPoolConfigReader: ConfigReader[ForkJoinPoolConfig] = deriveReader

  implicit val sslProtocolReader: ConfigReader[Protocol] = deriveEnumerationReader

  implicit val keyStoreTypeReader: ConfigReader[KeyStoreType] = deriveEnumerationReader

  implicit val keyStoreConfigReader: ConfigReader[KeyStoreConfig] = deriveReader

  implicit val sslContextConfigReader: ConfigReader[SslContextConfig] = deriveReader

}
