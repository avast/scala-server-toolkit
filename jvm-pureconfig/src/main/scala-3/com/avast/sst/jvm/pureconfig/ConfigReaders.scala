package com.avast.sst.jvm.pureconfig

import com.avast.sst.jvm.execution.ForkJoinPoolConfig.TaskPeekingMode
import com.avast.sst.jvm.execution.{ForkJoinPoolConfig, ThreadPoolExecutorConfig}
import pureconfig.ConfigReader
import pureconfig.generic.derivation.EnumConfigReader
import pureconfig.generic.derivation.default._

trait ConfigReaders {

  implicit val jvmThreadPoolExecutorConfigReader: ConfigReader[ThreadPoolExecutorConfig] =
    implicitly[ConfigReader[ThreadPoolExecutorConfig]]

  implicit val jvmTaskPeekingModeReader: ConfigReader[TaskPeekingMode] = implicitly[ConfigReader[TaskPeekingMode]]

  implicit val jvmForkJoinPoolConfigReader: ConfigReader[ForkJoinPoolConfig] = implicitly[ConfigReader[ForkJoinPoolConfig]]

}
