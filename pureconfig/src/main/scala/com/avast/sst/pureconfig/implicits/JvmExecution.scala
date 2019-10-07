package com.avast.sst.pureconfig.implicits

import com.avast.sst.execution.ForkJoinPoolConfig.TaskPeekingMode
import com.avast.sst.execution.{ForkJoinPoolConfig, ThreadPoolExecutorConfig}
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}

/** Implicit [[pureconfig.ConfigReader]] instances for `sst-jvm-execution` module.
  *
  * ```Do not forget``` to have a dependency on the `sst-jvm-execution` module in your project.
  */
object JvmExecution {

  implicit val threadPoolExecutorConfigReader: ConfigReader[ThreadPoolExecutorConfig] = deriveReader

  implicit val taskPeekingModeReader: ConfigReader[TaskPeekingMode] = deriveEnumerationReader

  implicit val forkJoinPoolConfigReader: ConfigReader[ForkJoinPoolConfig] = deriveReader

}
