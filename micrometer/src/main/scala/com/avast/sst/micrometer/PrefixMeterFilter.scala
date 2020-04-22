package com.avast.sst.micrometer

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.config.MeterFilter

class PrefixMeterFilter(prefix: String) extends MeterFilter {

  override def map(id: Meter.Id): Meter.Id = id.withName(s"$prefix${id.getName}")

}
