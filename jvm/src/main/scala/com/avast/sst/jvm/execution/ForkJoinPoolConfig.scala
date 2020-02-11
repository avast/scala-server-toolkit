package com.avast.sst.jvm.execution

import com.avast.sst.jvm.execution.ForkJoinPoolConfig.TaskPeekingMode
import com.avast.sst.jvm.execution.ForkJoinPoolConfig.TaskPeekingMode.{FIFO, LIFO}

final case class ForkJoinPoolConfig(
    parallelismMin: Int = 8,
    parallelismFactor: Double = 1.0,
    parallelismMax: Int = 64,
    taskPeekingMode: TaskPeekingMode = FIFO
) {

  private[sst] def computeParallelism(numOfCpus: Int): Int = {
    math.min(math.max(math.ceil(numOfCpus * parallelismFactor).toInt, parallelismMin), parallelismMax)
  }

  private[sst] def computeAsyncMode: Boolean = taskPeekingMode match {
    case FIFO => true
    case LIFO => false
  }

}

object ForkJoinPoolConfig {

  val Default: ForkJoinPoolConfig = ForkJoinPoolConfig()

  sealed trait TaskPeekingMode

  object TaskPeekingMode {

    case object FIFO extends TaskPeekingMode

    case object LIFO extends TaskPeekingMode

  }

}
