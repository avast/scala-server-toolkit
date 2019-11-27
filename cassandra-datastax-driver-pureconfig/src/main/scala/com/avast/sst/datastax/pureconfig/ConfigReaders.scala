package com.avast.sst.datastax.pureconfig

import com.avast.sst.datastax.config._
import pureconfig.{ConfigFieldMapping, ConfigReader, PascalCase}
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}

trait ConfigReaders {

  implicit val datastaxJavaDriverDatastaxConfigReader: ConfigReader[CassandraDatastaxDriverConfig] = deriveReader

  // Basic driver config
  implicit val datastaxJavaDriverBasicConfigReader: ConfigReader[BasicConfig] = deriveReader
  implicit val datastaxJavaDriverConsistencyLevelReader: ConfigReader[ConsistencyLevel] =
    deriveEnumerationReader[ConsistencyLevel](ConfigFieldMapping(PascalCase, PascalCase))
  implicit val datastaxJavaDriverBasicRequestConfigReader: ConfigReader[BasicRequestConfig] = deriveReader
  implicit val datastaxJavaDriverLoadBalancingConfigReader: ConfigReader[LoadBalancingPolicyConfig] = deriveReader
  implicit val datastaxJavaDriverFilterConfigReader: ConfigReader[FilterConfig] = deriveReader

  // Advanced driver config
  implicit val datastaxJavaDriverAdvancedConfigReader: ConfigReader[AdvancedConfig] = deriveReader
  implicit val datastaxJavaDriverRetryPolicyConfigReader: ConfigReader[RetryPolicyConfig] = deriveReader
  implicit val datastaxJavaDriverSpeculativeExecutionPolicyConfigReader: ConfigReader[SpeculativeExecutionPolicyConfig] = deriveReader
  implicit val datastaxJavaDriverTimestampGeneratorConfigReader: ConfigReader[TimestampGeneratorConfig] = deriveReader
  implicit val datastaxJavaDriverTraceConfigReader: ConfigReader[TraceConfig] = deriveReader
  implicit val datastaxJavaDriverDriftWarningConfigReader: ConfigReader[DriftWarningConfig] = deriveReader
  implicit val datastaxJavaDriverConnectionConfigReader: ConfigReader[ConnectionConfig] = deriveReader
  implicit val datastaxJavaDriverPoolConfigReader: ConfigReader[PoolConfig] = deriveReader
  implicit val datastaxJavaDriverReconnectionPolicyConfigReader: ConfigReader[ReconnectionPolicyConfig] = deriveReader
  implicit val datastaxJavaDriverAuthProviderConfigReader: ConfigReader[AuthProviderConfig] = deriveReader
  implicit val datastaxJavaDriverRequestTrackerConfigReader: ConfigReader[RequestTrackerConfig] = deriveReader
  implicit val datastaxJavaDriverLogsConfigReader: ConfigReader[LogsConfig] = deriveReader
  implicit val datastaxJavaDriverSlowConfigReader: ConfigReader[SlowConfig] = deriveReader
  implicit val datastaxJavaDriverThrottlerConfigReader: ConfigReader[ThrottlerConfig] = deriveReader
  implicit val datastaxJavaDriverNodeStateListenerConfigReader: ConfigReader[NodeStateListenerConfig] = deriveReader
  implicit val datastaxJavaDriverSchemaChangeListenerConfigReader: ConfigReader[SchemaChangeListenerConfig] = deriveReader
  implicit val datastaxJavaDriverAddressTranslatorConfigReader: ConfigReader[AddressTranslatorConfig] = deriveReader
  implicit val datastaxJavaDriverProtocolConfigReader: ConfigReader[ProtocolConfig] = deriveReader
  implicit val datastaxJavaDriverMetricsConfigReader: ConfigReader[MetricsConfig] = deriveReader
  implicit val datastaxJavaDriverSessionConfigReader: ConfigReader[SessionConfig] = deriveReader
  implicit val datastaxJavaDriverCqlRequestsConfigReader: ConfigReader[CqlRequestsConfig] = deriveReader
  implicit val datastaxJavaDriverThrottlingConfigReader: ConfigReader[ThrottlingConfig] = deriveReader
  implicit val datastaxJavaDriverDelayConfigReader: ConfigReader[DelayConfig] = deriveReader
  implicit val datastaxJavaDriverNodeConfigReader: ConfigReader[NodeConfig] = deriveReader
  implicit val datastaxJavaDriverCqlMessagesConfigReader: ConfigReader[CqlMessagesConfig] = deriveReader
  implicit val datastaxJavaDriverSocketConfigReader: ConfigReader[SocketConfig] = deriveReader
  implicit val datastaxJavaDriverHeartbeatConfigReader: ConfigReader[HeartbeatConfig] = deriveReader
  implicit val datastaxJavaDriverMetadataConfigReader: ConfigReader[MetadataConfig] = deriveReader
  implicit val datastaxJavaDriverTopologyEventDebouncerConfigReader: ConfigReader[TopologyEventDebouncerConfig] = deriveReader
  implicit val datastaxJavaDriverSchemaConfigReader: ConfigReader[SchemaConfig] = deriveReader
  implicit val datastaxJavaDriverDebouncerConfigReader: ConfigReader[DebouncerConfig] = deriveReader
  implicit val datastaxJavaDriverTokenMapConfigReader: ConfigReader[TokenMapConfig] = deriveReader
  implicit val datastaxJavaDriverControlConnectionConfigReader: ConfigReader[ControlConnectionConfig] = deriveReader
  implicit val datastaxJavaDriverSchemaAgreementConfigReader: ConfigReader[SchemaAgreementConfig] = deriveReader
  implicit val datastaxJavaDriverPreparedStatementsConfigReader: ConfigReader[PreparedStatementsConfig] = deriveReader
  implicit val datastaxJavaDriverReprepareOnUpConfigReader: ConfigReader[ReprepareOnUpConfig] = deriveReader
  implicit val datastaxJavaDriverNettyConfigReader: ConfigReader[NettyConfig] = deriveReader
  implicit val datastaxJavaDriverGroupConfigReader: ConfigReader[GroupConfig] = deriveReader
  implicit val datastaxJavaDriverShutdownConfigReader: ConfigReader[ShutdownConfig] = deriveReader
  implicit val datastaxJavaDriverTimerConfigReader: ConfigReader[TimerConfig] = deriveReader
  implicit val datastaxJavaDriverCoalescerConfigReader: ConfigReader[CoalescerConfig] = deriveReader
  implicit val datastaxJavaDriverAdvancedRequestConfigReader: ConfigReader[AdvancedRequestConfig] = deriveReader

  // Profile overridable config
  implicit val datastaxJavaDriverProfileConfigReader: ConfigReader[ProfileConfig] = deriveReader
  implicit val datastaxJavaDriverProfileBasicConfigReader: ConfigReader[ProfileBasicConfig] = deriveReader
  implicit val datastaxJavaDriverProfileAdvancedConfigReader: ConfigReader[ProfileAdvancedConfig] = deriveReader
  implicit val datastaxJavaDriverProfilePreparedStatementsConfigReader: ConfigReader[ProfilePreparedStatementsConfig] = deriveReader
  implicit val datastaxJavaDriverProfileProfileAdvancedRequestConfigReader: ConfigReader[ProfileAdvancedRequestConfig] = deriveReader
}
