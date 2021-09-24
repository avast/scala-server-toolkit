package com.avast.sst.datastax.pureconfig

import com.avast.sst.datastax.config._
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto._
import pureconfig.{ConfigFieldMapping, ConfigReader, PascalCase}

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val cassandraDatastaxDriverDatastaxConfigReader: ConfigReader[CassandraDatastaxDriverConfig] =
    deriveReader[CassandraDatastaxDriverConfig]

  // Basic driver config
  implicit val cassandraDatastaxDriverBasicConfigReader: ConfigReader[BasicConfig] = deriveReader[BasicConfig]
  implicit val cassandraDatastaxDriverConsistencyLevelReader: ConfigReader[ConsistencyLevel] =
    deriveEnumerationReader[ConsistencyLevel](ConfigFieldMapping(PascalCase, PascalCase))
  implicit val cassandraDatastaxDriverBasicRequestConfigReader: ConfigReader[BasicRequestConfig] = deriveReader[BasicRequestConfig]
  implicit val cassandraDatastaxDriverLoadBalancingConfigReader: ConfigReader[LoadBalancingPolicyConfig] =
    deriveReader[LoadBalancingPolicyConfig]
  implicit val cassandraDatastaxDriverFilterConfigReader: ConfigReader[FilterConfig] = deriveReader[FilterConfig]

  // Advanced driver config
  implicit val cassandraDatastaxDriverAdvancedConfigReader: ConfigReader[AdvancedConfig] = deriveReader[AdvancedConfig]
  implicit val cassandraDatastaxDriverRetryPolicyConfigReader: ConfigReader[RetryPolicyConfig] = deriveReader[RetryPolicyConfig]
  implicit val cassandraDatastaxDriverSpeculativeExecutionPolicyConfigReader: ConfigReader[SpeculativeExecutionPolicyConfig] =
    deriveReader[SpeculativeExecutionPolicyConfig]
  implicit val cassandraDatastaxDriverTimestampGeneratorConfigReader: ConfigReader[TimestampGeneratorConfig] =
    deriveReader[TimestampGeneratorConfig]
  implicit val cassandraDatastaxDriverTraceConfigReader: ConfigReader[TraceConfig] = deriveReader[TraceConfig]
  implicit val cassandraDatastaxDriverDriftWarningConfigReader: ConfigReader[DriftWarningConfig] = deriveReader[DriftWarningConfig]
  implicit val cassandraDatastaxDriverConnectionConfigReader: ConfigReader[ConnectionConfig] = deriveReader[ConnectionConfig]
  implicit val cassandraDatastaxDriverPoolConfigReader: ConfigReader[PoolConfig] = deriveReader[PoolConfig]
  implicit val cassandraDatastaxDriverReconnectionPolicyConfigReader: ConfigReader[ReconnectionPolicyConfig] =
    deriveReader[ReconnectionPolicyConfig]
  implicit val cassandraDatastaxDriverAuthProviderConfigReader: ConfigReader[AuthProviderConfig] = deriveReader[AuthProviderConfig]
  implicit val cassandraDatastaxDriverRequestTrackerConfigReader: ConfigReader[RequestTrackerConfig] = deriveReader[RequestTrackerConfig]
  implicit val cassandraDatastaxDriverLogsConfigReader: ConfigReader[LogsConfig] = deriveReader[LogsConfig]
  implicit val cassandraDatastaxDriverSlowConfigReader: ConfigReader[SlowConfig] = deriveReader[SlowConfig]
  implicit val cassandraDatastaxDriverThrottlerConfigReader: ConfigReader[ThrottlerConfig] = deriveReader[ThrottlerConfig]
  implicit val cassandraDatastaxDriverNodeStateListenerConfigReader: ConfigReader[NodeStateListenerConfig] =
    deriveReader[NodeStateListenerConfig]
  implicit val cassandraDatastaxDriverSchemaChangeListenerConfigReader: ConfigReader[SchemaChangeListenerConfig] =
    deriveReader[SchemaChangeListenerConfig]
  implicit val cassandraDatastaxDriverAddressTranslatorConfigReader: ConfigReader[AddressTranslatorConfig] =
    deriveReader[AddressTranslatorConfig]
  implicit val cassandraDatastaxDriverProtocolConfigReader: ConfigReader[ProtocolConfig] = deriveReader[ProtocolConfig]
  implicit val cassandraDatastaxDriverMetricsConfigReader: ConfigReader[MetricsConfig] = deriveReader[MetricsConfig]
  implicit val cassandraDatastaxDriverSessionConfigReader: ConfigReader[SessionConfig] = deriveReader[SessionConfig]
  implicit val cassandraDatastaxDriverCqlRequestsConfigReader: ConfigReader[CqlRequestsConfig] = deriveReader[CqlRequestsConfig]
  implicit val cassandraDatastaxDriverThrottlingConfigReader: ConfigReader[ThrottlingConfig] = deriveReader[ThrottlingConfig]
  implicit val cassandraDatastaxDriverDelayConfigReader: ConfigReader[DelayConfig] = deriveReader[DelayConfig]
  implicit val cassandraDatastaxDriverNodeConfigReader: ConfigReader[NodeConfig] = deriveReader[NodeConfig]
  implicit val cassandraDatastaxDriverCqlMessagesConfigReader: ConfigReader[CqlMessagesConfig] = deriveReader[CqlMessagesConfig]
  implicit val cassandraDatastaxDriverSocketConfigReader: ConfigReader[SocketConfig] = deriveReader[SocketConfig]
  implicit val cassandraDatastaxDriverHeartbeatConfigReader: ConfigReader[HeartbeatConfig] = deriveReader[HeartbeatConfig]
  implicit val cassandraDatastaxDriverMetadataConfigReader: ConfigReader[MetadataConfig] = deriveReader[MetadataConfig]
  implicit val cassandraDatastaxDriverTopologyEventDebouncerConfigReader: ConfigReader[TopologyEventDebouncerConfig] =
    deriveReader[TopologyEventDebouncerConfig]
  implicit val cassandraDatastaxDriverSchemaConfigReader: ConfigReader[SchemaConfig] = deriveReader[SchemaConfig]
  implicit val cassandraDatastaxDriverDebouncerConfigReader: ConfigReader[DebouncerConfig] = deriveReader[DebouncerConfig]
  implicit val cassandraDatastaxDriverTokenMapConfigReader: ConfigReader[TokenMapConfig] = deriveReader[TokenMapConfig]
  implicit val cassandraDatastaxDriverControlConnectionConfigReader: ConfigReader[ControlConnectionConfig] =
    deriveReader[ControlConnectionConfig]
  implicit val cassandraDatastaxDriverSchemaAgreementConfigReader: ConfigReader[SchemaAgreementConfig] = deriveReader[SchemaAgreementConfig]
  implicit val cassandraDatastaxDriverPreparedStatementsConfigReader: ConfigReader[PreparedStatementsConfig] =
    deriveReader[PreparedStatementsConfig]
  implicit val cassandraDatastaxDriverReprepareOnUpConfigReader: ConfigReader[ReprepareOnUpConfig] = deriveReader[ReprepareOnUpConfig]
  implicit val cassandraDatastaxDriverNettyConfigReader: ConfigReader[NettyConfig] = deriveReader[NettyConfig]
  implicit val cassandraDatastaxDriverGroupConfigReader: ConfigReader[GroupConfig] = deriveReader[GroupConfig]
  implicit val cassandraDatastaxDriverShutdownConfigReader: ConfigReader[ShutdownConfig] = deriveReader[ShutdownConfig]
  implicit val cassandraDatastaxDriverTimerConfigReader: ConfigReader[TimerConfig] = deriveReader[TimerConfig]
  implicit val cassandraDatastaxDriverCoalescerConfigReader: ConfigReader[CoalescerConfig] = deriveReader[CoalescerConfig]
  implicit val cassandraDatastaxDriverAdvancedRequestConfigReader: ConfigReader[AdvancedRequestConfig] = deriveReader[AdvancedRequestConfig]

  // Profile overridable config
  implicit val cassandraDatastaxDriverProfileConfigReader: ConfigReader[ProfileConfig] = deriveReader[ProfileConfig]
  implicit val cassandraDatastaxDriverProfileBasicConfigReader: ConfigReader[ProfileBasicConfig] = deriveReader[ProfileBasicConfig]
  implicit val cassandraDatastaxDriverProfileAdvancedConfigReader: ConfigReader[ProfileAdvancedConfig] = deriveReader[ProfileAdvancedConfig]
  implicit val cassandraDatastaxDriverProfilePreparedStatementsConfigReader: ConfigReader[ProfilePreparedStatementsConfig] =
    deriveReader[ProfilePreparedStatementsConfig]
  implicit val cassandraDatastaxDriverProfileProfileAdvancedRequestConfigReader: ConfigReader[ProfileAdvancedRequestConfig] =
    deriveReader[ProfileAdvancedRequestConfig]
}
