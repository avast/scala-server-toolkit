package com.avast.sst.micrometer.prometheus

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

final case class MicrometerPrometheusConfig(
    step: Duration = Duration(1, TimeUnit.MINUTES),
    prefix: String = "",
    descriptions: Boolean = true,
    commonTags: Map[String, String] = Map.empty
)
