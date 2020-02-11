package com.avast.sst.datastax

import scala.concurrent.duration._

package object config {
  val InitQueryTimeout: Duration = 500.milliseconds
  val RequestTimeout: Duration = 2.seconds
  val RequestPageSize: Int = 5000
}
