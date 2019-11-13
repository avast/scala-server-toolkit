package com.avast.sst.datastax.config

import scala.concurrent.duration.Duration

/** Profile configuration holding overridable properties.
  */
final case class Profile(name: String, basic: Option[ProfileBasic], advanced: Option[ProfileAdvanced])

final case class ProfileBasic(request: Option[ProfileBasicRequest], loadBalancingPolicy: Option[LoadBalancingPolicy])

final case class ProfileBasicRequest(timeout: Option[Duration],
                                     consistency: Option[String],
                                     pageSize: Option[Int],
                                     serialConsistency: Option[String],
                                     defaultIdempotence: Option[Boolean])

final case class ProfileAdvanced(request: Option[ProfileAdvancedRequest],
                                 retryPolicy: Option[RetryPolicy],
                                 speculativeExecutionPolicy: Option[SpeculativeExecutionPolicy],
                                 timestampGenerator: Option[TimestampGenerator],
                                 preparedStatements: Option[ProfilePreparedStatements])

final case class ProfileAdvancedRequest(trace: Option[ProfileTrace], logWarnings: Option[Boolean])

final case class ProfileTrace(attempts: Option[Int], interval: Option[Duration], consistency: Option[String])

final case class ProfilePreparedStatements(prepareOnAllNodes: Option[Boolean])
