package com.avast.server.toolkit.execution

import ForkJoinPoolConfig.TaskPeekingMode
import ForkJoinPoolConfig.TaskPeekingMode.{FIFO, LIFO}

@SuppressWarnings(Array("org.wartremover.warts.DefaultArguments"))
final case class ForkJoinPoolConfig(parallelismMin: Int = 8,
                                    parallelismFactor: Double = 1.0,
                                    parallelismMax: Int = 64,
                                    taskPeekingMode: TaskPeekingMode = FIFO) {

  private[toolkit] def computeParallelism(numOfCpus: Int): Int = {
    math.min(math.max(math.ceil(numOfCpus * parallelismFactor).toInt, parallelismMin), parallelismMax)
  }

  private[toolkit] def computeAsyncMode: Boolean = taskPeekingMode match {
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
