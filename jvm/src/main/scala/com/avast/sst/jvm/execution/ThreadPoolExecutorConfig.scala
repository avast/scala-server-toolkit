package com.avast.sst.jvm.execution

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, FiniteDuration}

final case class ThreadPoolExecutorConfig(
    coreSize: Int,
    maxSize: Int,
    keepAlive: FiniteDuration = Duration(60000L, TimeUnit.MILLISECONDS),
    allowCoreThreadTimeout: Boolean = false
)
