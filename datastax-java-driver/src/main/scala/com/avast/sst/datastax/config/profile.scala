package com.avast.sst.datastax.config

/** Profile configuration holding overridable properties.
  */
final case class Profile(name: String, basic: ProfileBasic = ProfileBasic.Default, advanced: ProfileAdvanced = ProfileAdvanced.Default)

final case class ProfileBasic(request: BasicRequest = ProfileBasic.Default.request,
                              loadBalancingPolicy: LoadBalancingPolicy = ProfileBasic.Default.loadBalancingPolicy)

object ProfileBasic {
  val Default: ProfileBasic = ProfileBasic(BasicRequest.Default, LoadBalancingPolicy.Default)
}

final case class ProfileAdvanced(
  request: ProfileAdvancedRequest = ProfileAdvanced.Default.request,
  retryPolicy: RetryPolicy = ProfileAdvanced.Default.retryPolicy,
  speculativeExecutionPolicy: SpeculativeExecutionPolicy = ProfileAdvanced.Default.speculativeExecutionPolicy,
  timestampGenerator: TimestampGenerator = ProfileAdvanced.Default.timestampGenerator,
  preparedStatements: ProfilePreparedStatements = ProfileAdvanced.Default.preparedStatements
)

object ProfileAdvanced {
  val Default: ProfileAdvanced = ProfileAdvanced(ProfileAdvancedRequest.Default,
                                                 RetryPolicy.Default,
                                                 SpeculativeExecutionPolicy.Default,
                                                 TimestampGenerator.Default,
                                                 ProfilePreparedStatements.Default)
}

final case class ProfileAdvancedRequest(trace: Trace = ProfileAdvancedRequest.Default.trace,
                                        logWarnings: Boolean = ProfileAdvancedRequest.Default.logWarnings)

object ProfileAdvancedRequest {
  val Default: ProfileAdvancedRequest = ProfileAdvancedRequest(Trace.Default, true)
}

final case class ProfilePreparedStatements(prepareOnAllNodes: Boolean)

object ProfilePreparedStatements {
  val Default: ProfilePreparedStatements = ProfilePreparedStatements(true)
}
