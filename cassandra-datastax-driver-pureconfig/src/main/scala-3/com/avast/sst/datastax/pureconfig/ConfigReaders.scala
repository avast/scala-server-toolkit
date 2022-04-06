package com.avast.sst.datastax.pureconfig

import com.avast.sst.datastax.config._
import pureconfig.{ConfigFieldMapping, ConfigReader}

trait ConfigReaders {

  implicit val cassandraDatastaxDriverDatastaxConfigReader: ConfigReader[CassandraDatastaxDriverConfig] =
    implicitly[ConfigReader[CassandraDatastaxDriverConfig]]

  // Basic driver config
  implicit val cassandraDatastaxDriverBasicConfigReader: ConfigReader[BasicConfig] = implicitly[ConfigReader[BasicConfig]]
  implicit val cassandraDatastaxDriverConsistencyLevelReader: ConfigReader[ConsistencyLevel] =
    implicitly[ConfigReader[ConsistencyLevel]]
  implicit val cassandraDatastaxDriverBasicRequestConfigReader: ConfigReader[BasicRequestConfig] =
    implicitly[ConfigReader[BasicRequestConfig]]
  implicit val cassandraDatastaxDriverLoadBalancingConfigReader: ConfigReader[LoadBalancingPolicyConfig] =
    implicitly[ConfigReader[LoadBalancingPolicyConfig]]
  implicit val cassandraDatastaxDriverFilterConfigReader: ConfigReader[FilterConfig] = implicitly[ConfigReader[FilterConfig]]

  // Advanced driver config
  implicit val cassandraDatastaxDriverAdvancedConfigReader: ConfigReader[AdvancedConfig] = implicitly[ConfigReader[AdvancedConfig]]
  implicit val cassandraDatastaxDriverRetryPolicyConfigReader: ConfigReader[RetryPolicyConfig] = implicitly[ConfigReader[RetryPolicyConfig]]
  implicit val cassandraDatastaxDriverSpeculativeExecutionPolicyConfigReader: ConfigReader[SpeculativeExecutionPolicyConfig] =
    implicitly[ConfigReader[SpeculativeExecutionPolicyConfig]]
  implicit val cassandraDatastaxDriverTimestampGeneratorConfigReader: ConfigReader[TimestampGeneratorConfig] =
    implicitly[ConfigReader[TimestampGeneratorConfig]]
  implicit val cassandraDatastaxDriverTraceConfigReader: ConfigReader[TraceConfig] = implicitly[ConfigReader[TraceConfig]]
  implicit val cassandraDatastaxDriverDriftWarningConfigReader: ConfigReader[DriftWarningConfig] =
    implicitly[ConfigReader[DriftWarningConfig]]
  implicit val cassandraDatastaxDriverConnectionConfigReader: ConfigReader[ConnectionConfig] = implicitly[ConfigReader[ConnectionConfig]]
  implicit val cassandraDatastaxDriverPoolConfigReader: ConfigReader[PoolConfig] = implicitly[ConfigReader[PoolConfig]]
  implicit val cassandraDatastaxDriverReconnectionPolicyConfigReader: ConfigReader[ReconnectionPolicyConfig] =
    implicitly[ConfigReader[ReconnectionPolicyConfig]]
  implicit val cassandraDatastaxDriverAuthProviderConfigReader: ConfigReader[AuthProviderConfig] =
    implicitly[ConfigReader[AuthProviderConfig]]
  implicit val cassandraDatastaxDriverRequestTrackerConfigReader: ConfigReader[RequestTrackerConfig] =
    implicitly[ConfigReader[RequestTrackerConfig]]
  implicit val cassandraDatastaxDriverLogsConfigReader: ConfigReader[LogsConfig] = implicitly[ConfigReader[LogsConfig]]
  implicit val cassandraDatastaxDriverSlowConfigReader: ConfigReader[SlowConfig] = implicitly[ConfigReader[SlowConfig]]
  implicit val cassandraDatastaxDriverThrottlerConfigReader: ConfigReader[ThrottlerConfig] = implicitly[ConfigReader[ThrottlerConfig]]
  implicit val cassandraDatastaxDriverNodeStateListenerConfigReader: ConfigReader[NodeStateListenerConfig] =
    implicitly[ConfigReader[NodeStateListenerConfig]]
  implicit val cassandraDatastaxDriverSchemaChangeListenerConfigReader: ConfigReader[SchemaChangeListenerConfig] =
    implicitly[ConfigReader[SchemaChangeListenerConfig]]
  implicit val cassandraDatastaxDriverAddressTranslatorConfigReader: ConfigReader[AddressTranslatorConfig] =
    implicitly[ConfigReader[AddressTranslatorConfig]]
  implicit val cassandraDatastaxDriverProtocolConfigReader: ConfigReader[ProtocolConfig] = implicitly[ConfigReader[ProtocolConfig]]
  implicit val cassandraDatastaxDriverMetricsConfigReader: ConfigReader[MetricsConfig] = implicitly[ConfigReader[MetricsConfig]]
  implicit val cassandraDatastaxDriverSessionConfigReader: ConfigReader[SessionConfig] = implicitly[ConfigReader[SessionConfig]]
  implicit val cassandraDatastaxDriverCqlRequestsConfigReader: ConfigReader[CqlRequestsConfig] = implicitly[ConfigReader[CqlRequestsConfig]]
  implicit val cassandraDatastaxDriverThrottlingConfigReader: ConfigReader[ThrottlingConfig] = implicitly[ConfigReader[ThrottlingConfig]]
  implicit val cassandraDatastaxDriverDelayConfigReader: ConfigReader[DelayConfig] = implicitly[ConfigReader[DelayConfig]]
  implicit val cassandraDatastaxDriverNodeConfigReader: ConfigReader[NodeConfig] = implicitly[ConfigReader[NodeConfig]]
  implicit val cassandraDatastaxDriverCqlMessagesConfigReader: ConfigReader[CqlMessagesConfig] = implicitly[ConfigReader[CqlMessagesConfig]]
  implicit val cassandraDatastaxDriverSocketConfigReader: ConfigReader[SocketConfig] = implicitly[ConfigReader[SocketConfig]]
  implicit val cassandraDatastaxDriverHeartbeatConfigReader: ConfigReader[HeartbeatConfig] = implicitly[ConfigReader[HeartbeatConfig]]
  implicit val cassandraDatastaxDriverMetadataConfigReader: ConfigReader[MetadataConfig] = implicitly[ConfigReader[MetadataConfig]]
  implicit val cassandraDatastaxDriverTopologyEventDebouncerConfigReader: ConfigReader[TopologyEventDebouncerConfig] =
    implicitly[ConfigReader[TopologyEventDebouncerConfig]]
  implicit val cassandraDatastaxDriverSchemaConfigReader: ConfigReader[SchemaConfig] = implicitly[ConfigReader[SchemaConfig]]
  implicit val cassandraDatastaxDriverDebouncerConfigReader: ConfigReader[DebouncerConfig] = implicitly[ConfigReader[DebouncerConfig]]
  implicit val cassandraDatastaxDriverTokenMapConfigReader: ConfigReader[TokenMapConfig] = implicitly[ConfigReader[TokenMapConfig]]
  implicit val cassandraDatastaxDriverControlConnectionConfigReader: ConfigReader[ControlConnectionConfig] =
    implicitly[ConfigReader[ControlConnectionConfig]]
  implicit val cassandraDatastaxDriverSchemaAgreementConfigReader: ConfigReader[SchemaAgreementConfig] =
    implicitly[ConfigReader[SchemaAgreementConfig]]
  implicit val cassandraDatastaxDriverPreparedStatementsConfigReader: ConfigReader[PreparedStatementsConfig] =
    implicitly[ConfigReader[PreparedStatementsConfig]]
  implicit val cassandraDatastaxDriverReprepareOnUpConfigReader: ConfigReader[ReprepareOnUpConfig] =
    implicitly[ConfigReader[ReprepareOnUpConfig]]
  implicit val cassandraDatastaxDriverNettyConfigReader: ConfigReader[NettyConfig] = implicitly[ConfigReader[NettyConfig]]
  implicit val cassandraDatastaxDriverGroupConfigReader: ConfigReader[GroupConfig] = implicitly[ConfigReader[GroupConfig]]
  implicit val cassandraDatastaxDriverShutdownConfigReader: ConfigReader[ShutdownConfig] = implicitly[ConfigReader[ShutdownConfig]]
  implicit val cassandraDatastaxDriverTimerConfigReader: ConfigReader[TimerConfig] = implicitly[ConfigReader[TimerConfig]]
  implicit val cassandraDatastaxDriverCoalescerConfigReader: ConfigReader[CoalescerConfig] = implicitly[ConfigReader[CoalescerConfig]]
  implicit val cassandraDatastaxDriverAdvancedRequestConfigReader: ConfigReader[AdvancedRequestConfig] =
    implicitly[ConfigReader[AdvancedRequestConfig]]

  // Profile overridable config
  implicit val cassandraDatastaxDriverProfileConfigReader: ConfigReader[ProfileConfig] = implicitly[ConfigReader[ProfileConfig]]
  implicit val cassandraDatastaxDriverProfileBasicConfigReader: ConfigReader[ProfileBasicConfig] =
    implicitly[ConfigReader[ProfileBasicConfig]]
  implicit val cassandraDatastaxDriverProfileAdvancedConfigReader: ConfigReader[ProfileAdvancedConfig] =
    implicitly[ConfigReader[ProfileAdvancedConfig]]
  implicit val cassandraDatastaxDriverProfilePreparedStatementsConfigReader: ConfigReader[ProfilePreparedStatementsConfig] =
    implicitly[ConfigReader[ProfilePreparedStatementsConfig]]
  implicit val cassandraDatastaxDriverProfileProfileAdvancedRequestConfigReader: ConfigReader[ProfileAdvancedRequestConfig] =
    implicitly[ConfigReader[ProfileAdvancedRequestConfig]]
}
