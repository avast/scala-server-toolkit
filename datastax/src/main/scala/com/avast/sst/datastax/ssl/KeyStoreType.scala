package com.avast.sst.datastax.ssl

import cats.Show

sealed trait KeyStoreType

object KeyStoreType {

  object JKS extends KeyStoreType

  object PKCS12 extends KeyStoreType

  implicit val show: Show[KeyStoreType] = {
    case JKS    => "jks"
    case PKCS12 => "pkcs12"
  }

}
