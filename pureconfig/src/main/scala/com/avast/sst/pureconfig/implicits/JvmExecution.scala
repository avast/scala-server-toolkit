package com.avast.sst.pureconfig.implicits

import com.avast.sst.execution.ForkJoinPoolConfig.TaskPeekingMode
import com.avast.sst.execution.{ForkJoinPoolConfig, ThreadPoolExecutorConfig}
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.semiauto.deriveReader

object JvmExecution {

  implicit val threadPoolExecutorConfigReader: ConfigReader[ThreadPoolExecutorConfig] = deriveReader

  implicit val taskPeekingModeReader: ConfigReader[TaskPeekingMode] = ConfigReader[String].map(_.toLowerCase).emap {
    case "fifo"   => Right(TaskPeekingMode.FIFO)
    case "lifo"   => Right(TaskPeekingMode.LIFO)
    case badValue => Left(CannotConvert(badValue, "TaskPeekingMode", "FIFO|LIFO"))
  }

  implicit val forkJoinPoolConfigReader: ConfigReader[ForkJoinPoolConfig] = deriveReader

}
