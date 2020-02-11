package com.avast.sst.datastax.config

/** Profile configuration holding overridable properties.
  */
final case class ProfileConfig(
    name: String,
    basic: ProfileBasicConfig = ProfileBasicConfig.Default,
    advanced: ProfileAdvancedConfig = ProfileAdvancedConfig.Default
)

final case class ProfileBasicConfig(
    request: BasicRequestConfig = ProfileBasicConfig.Default.request,
    loadBalancingPolicy: LoadBalancingPolicyConfig = ProfileBasicConfig.Default.loadBalancingPolicy
)

object ProfileBasicConfig {
  val Default: ProfileBasicConfig = ProfileBasicConfig(BasicRequestConfig.Default, LoadBalancingPolicyConfig.Default)
}

final case class ProfileAdvancedConfig(
    request: ProfileAdvancedRequestConfig = ProfileAdvancedConfig.Default.request,
    retryPolicy: RetryPolicyConfig = ProfileAdvancedConfig.Default.retryPolicy,
    speculativeExecutionPolicy: SpeculativeExecutionPolicyConfig = ProfileAdvancedConfig.Default.speculativeExecutionPolicy,
    timestampGenerator: TimestampGeneratorConfig = ProfileAdvancedConfig.Default.timestampGenerator,
    preparedStatements: ProfilePreparedStatementsConfig = ProfileAdvancedConfig.Default.preparedStatements
)

object ProfileAdvancedConfig {
  val Default: ProfileAdvancedConfig = ProfileAdvancedConfig(
    ProfileAdvancedRequestConfig.Default,
    RetryPolicyConfig.Default,
    SpeculativeExecutionPolicyConfig.Default,
    TimestampGeneratorConfig.Default,
    ProfilePreparedStatementsConfig.Default
  )
}

final case class ProfileAdvancedRequestConfig(
    trace: TraceConfig = ProfileAdvancedRequestConfig.Default.trace,
    logWarnings: Boolean = ProfileAdvancedRequestConfig.Default.logWarnings
)

object ProfileAdvancedRequestConfig {
  val Default: ProfileAdvancedRequestConfig = ProfileAdvancedRequestConfig(TraceConfig.Default, true)
}

final case class ProfilePreparedStatementsConfig(prepareOnAllNodes: Boolean)

object ProfilePreparedStatementsConfig {
  val Default: ProfilePreparedStatementsConfig = ProfilePreparedStatementsConfig(true)
}
