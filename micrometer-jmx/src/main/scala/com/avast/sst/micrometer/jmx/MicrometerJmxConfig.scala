package com.avast.sst.micrometer.jmx

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

final case class MicrometerJmxConfig(
    domain: String,
    enableTypeScopeNameHierarchy: Boolean = false,
    step: Duration = Duration(1, TimeUnit.MINUTES)
)
