package com.avast.sst.datastax

import scala.concurrent.duration._
import scala.language.postfixOps

package object config {
  val INIT_QUERY_TIMEOUT: Duration = 500 milliseconds
  val REQUEST_TIMEOUT: Duration = 2 seconds
  val REQUEST_PAGE_SIZE: Int = 5000
  val SERIAL_CONSISTENCY_LEVEL_SERIAL: String = "SERIAL"
  val CONSISTENCY_LEVEL_LOCAL_ONE: String = "LOCAL_ONE"
  val CONSISTENCY_LEVEL_ONE: String = "ONE"
  val `256 MB`: Int = 268435456
}
