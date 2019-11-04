package com.avast.sst.http4s.client.monix.catnap

import org.http4s.Status

/** Classifies HTTP status as failure or not - for the purpose of circuit breaking. */
trait HttpStatusClassifier {

  def isFailure(status: Status): Boolean

}

object HttpStatusClassifier {

  private val defaultFailureStatuses = fromSet(Set(Status.TooManyRequests))

  lazy val default: HttpStatusClassifier = s => status5xx.isFailure(s) || defaultFailureStatuses.isFailure(s)

  lazy val status5xx: HttpStatusClassifier = _.code >= 500

  def fromSet(failureStatuses: Set[Status]): HttpStatusClassifier = failureStatuses(_)

}
