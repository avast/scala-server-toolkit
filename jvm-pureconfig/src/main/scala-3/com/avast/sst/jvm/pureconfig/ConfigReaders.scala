package com.avast.sst.jvm.pureconfig

import com.avast.sst.jvm.execution.ForkJoinPoolConfig.TaskPeekingMode
import com.avast.sst.jvm.execution.{ForkJoinPoolConfig, ThreadPoolExecutorConfig}
import pureconfig.ConfigReader
import pureconfig.generic.derivation.EnumConfigReader
import pureconfig.generic.derivation.default.*

trait ConfigReaders {

  implicit val jvmThreadPoolExecutorConfigReader: ConfigReader[ThreadPoolExecutorConfig] =
    ConfigReader.derived

  implicit val jvmTaskPeekingModeReader: ConfigReader[TaskPeekingMode] = ConfigReader.derived

  implicit val jvmForkJoinPoolConfigReader: ConfigReader[ForkJoinPoolConfig] = ConfigReader.derived

}
