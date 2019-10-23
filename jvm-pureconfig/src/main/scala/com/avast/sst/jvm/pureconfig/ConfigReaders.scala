package com.avast.sst.jvm.pureconfig

import com.avast.sst.jvm.execution.ForkJoinPoolConfig.TaskPeekingMode
import com.avast.sst.jvm.execution.{ForkJoinPoolConfig, ThreadPoolExecutorConfig}
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}

trait ConfigReaders {

  implicit val jvmThreadPoolExecutorConfigReader: ConfigReader[ThreadPoolExecutorConfig] = deriveReader

  implicit val jvmTaskPeekingModeReader: ConfigReader[TaskPeekingMode] = deriveEnumerationReader

  implicit val jvmForkJoinPoolConfigReader: ConfigReader[ForkJoinPoolConfig] = deriveReader

}
