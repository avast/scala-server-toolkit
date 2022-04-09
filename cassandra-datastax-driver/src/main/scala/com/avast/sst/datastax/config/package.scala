package com.avast.sst.datastax

import scala.concurrent.duration.*

package object config {
  val ConnectTimeout: Duration = 5.seconds
  val InitQueryTimeout: Duration = 5.seconds
  val RequestTimeout: Duration = 2.seconds
  val RequestPageSize: Int = 5000
}
