package com.avast.sst.datastax.config

import scala.concurrent.duration._

/** Profile configuration holding overridable properties.
  */
final case class Profile(name: String, basic: ProfileBasic = ProfileBasic(), advanced: ProfileAdvanced = ProfileAdvanced())

final case class ProfileBasic(request: BasicRequest = BasicRequest(), loadBalancingPolicy: LoadBalancingPolicy = LoadBalancingPolicy())

final case class ProfileAdvanced(request: ProfileAdvancedRequest = ProfileAdvancedRequest(),
                                 retryPolicy: RetryPolicy = RetryPolicy(),
                                 speculativeExecutionPolicy: SpeculativeExecutionPolicy = SpeculativeExecutionPolicy(),
                                 timestampGenerator: TimestampGenerator = TimestampGenerator(),
                                 preparedStatements: ProfilePreparedStatements = ProfilePreparedStatements())

final case class ProfileAdvancedRequest(trace: Trace = Trace(5, 3.milliseconds, ConsistencyLevel.LocalOne), logWarnings: Boolean = true)

final case class ProfilePreparedStatements(prepareOnAllNodes: Boolean = true)
