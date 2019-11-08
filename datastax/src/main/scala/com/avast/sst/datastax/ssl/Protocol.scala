package com.avast.sst.datastax.ssl

import cats.Show

sealed trait Protocol

object Protocol {

  case object TLS extends Protocol

  case object SSL extends Protocol

  implicit val show: Show[Protocol] = {
    case TLS => "TLS"
    case SSL => "SSL"
  }

}
