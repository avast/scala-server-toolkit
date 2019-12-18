package com.avast.sst.datastax.pureconfig

import com.avast.sst.datastax.config._
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}
import pureconfig.{ConfigFieldMapping, ConfigReader, PascalCase}

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val cassandraDatastaxDriverDatastaxConfigReader: ConfigReader[CassandraDatastaxDriverConfig] = deriveReader

  // Basic driver config
  implicit val cassandraDatastaxDriverBasicConfigReader: ConfigReader[BasicConfig] = deriveReader
  implicit val cassandraDatastaxDriverConsistencyLevelReader: ConfigReader[ConsistencyLevel] =
    deriveEnumerationReader[ConsistencyLevel](ConfigFieldMapping(PascalCase, PascalCase))
  implicit val cassandraDatastaxDriverBasicRequestConfigReader: ConfigReader[BasicRequestConfig] = deriveReader
  implicit val cassandraDatastaxDriverLoadBalancingConfigReader: ConfigReader[LoadBalancingPolicyConfig] = deriveReader
  implicit val cassandraDatastaxDriverFilterConfigReader: ConfigReader[FilterConfig] = deriveReader

  // Advanced driver config
  implicit val cassandraDatastaxDriverAdvancedConfigReader: ConfigReader[AdvancedConfig] = deriveReader
  implicit val cassandraDatastaxDriverRetryPolicyConfigReader: ConfigReader[RetryPolicyConfig] = deriveReader
  implicit val cassandraDatastaxDriverSpeculativeExecutionPolicyConfigReader: ConfigReader[SpeculativeExecutionPolicyConfig] = deriveReader
  implicit val cassandraDatastaxDriverTimestampGeneratorConfigReader: ConfigReader[TimestampGeneratorConfig] = deriveReader
  implicit val cassandraDatastaxDriverTraceConfigReader: ConfigReader[TraceConfig] = deriveReader
  implicit val cassandraDatastaxDriverDriftWarningConfigReader: ConfigReader[DriftWarningConfig] = deriveReader
  implicit val cassandraDatastaxDriverConnectionConfigReader: ConfigReader[ConnectionConfig] = deriveReader
  implicit val cassandraDatastaxDriverPoolConfigReader: ConfigReader[PoolConfig] = deriveReader
  implicit val cassandraDatastaxDriverReconnectionPolicyConfigReader: ConfigReader[ReconnectionPolicyConfig] = deriveReader
  implicit val cassandraDatastaxDriverAuthProviderConfigReader: ConfigReader[AuthProviderConfig] = deriveReader
  implicit val cassandraDatastaxDriverRequestTrackerConfigReader: ConfigReader[RequestTrackerConfig] = deriveReader
  implicit val cassandraDatastaxDriverLogsConfigReader: ConfigReader[LogsConfig] = deriveReader
  implicit val cassandraDatastaxDriverSlowConfigReader: ConfigReader[SlowConfig] = deriveReader
  implicit val cassandraDatastaxDriverThrottlerConfigReader: ConfigReader[ThrottlerConfig] = deriveReader
  implicit val cassandraDatastaxDriverNodeStateListenerConfigReader: ConfigReader[NodeStateListenerConfig] = deriveReader
  implicit val cassandraDatastaxDriverSchemaChangeListenerConfigReader: ConfigReader[SchemaChangeListenerConfig] = deriveReader
  implicit val cassandraDatastaxDriverAddressTranslatorConfigReader: ConfigReader[AddressTranslatorConfig] = deriveReader
  implicit val cassandraDatastaxDriverProtocolConfigReader: ConfigReader[ProtocolConfig] = deriveReader
  implicit val cassandraDatastaxDriverMetricsConfigReader: ConfigReader[MetricsConfig] = deriveReader
  implicit val cassandraDatastaxDriverSessionConfigReader: ConfigReader[SessionConfig] = deriveReader
  implicit val cassandraDatastaxDriverCqlRequestsConfigReader: ConfigReader[CqlRequestsConfig] = deriveReader
  implicit val cassandraDatastaxDriverThrottlingConfigReader: ConfigReader[ThrottlingConfig] = deriveReader
  implicit val cassandraDatastaxDriverDelayConfigReader: ConfigReader[DelayConfig] = deriveReader
  implicit val cassandraDatastaxDriverNodeConfigReader: ConfigReader[NodeConfig] = deriveReader
  implicit val cassandraDatastaxDriverCqlMessagesConfigReader: ConfigReader[CqlMessagesConfig] = deriveReader
  implicit val cassandraDatastaxDriverSocketConfigReader: ConfigReader[SocketConfig] = deriveReader
  implicit val cassandraDatastaxDriverHeartbeatConfigReader: ConfigReader[HeartbeatConfig] = deriveReader
  implicit val cassandraDatastaxDriverMetadataConfigReader: ConfigReader[MetadataConfig] = deriveReader
  implicit val cassandraDatastaxDriverTopologyEventDebouncerConfigReader: ConfigReader[TopologyEventDebouncerConfig] = deriveReader
  implicit val cassandraDatastaxDriverSchemaConfigReader: ConfigReader[SchemaConfig] = deriveReader
  implicit val cassandraDatastaxDriverDebouncerConfigReader: ConfigReader[DebouncerConfig] = deriveReader
  implicit val cassandraDatastaxDriverTokenMapConfigReader: ConfigReader[TokenMapConfig] = deriveReader
  implicit val cassandraDatastaxDriverControlConnectionConfigReader: ConfigReader[ControlConnectionConfig] = deriveReader
  implicit val cassandraDatastaxDriverSchemaAgreementConfigReader: ConfigReader[SchemaAgreementConfig] = deriveReader
  implicit val cassandraDatastaxDriverPreparedStatementsConfigReader: ConfigReader[PreparedStatementsConfig] = deriveReader
  implicit val cassandraDatastaxDriverReprepareOnUpConfigReader: ConfigReader[ReprepareOnUpConfig] = deriveReader
  implicit val cassandraDatastaxDriverNettyConfigReader: ConfigReader[NettyConfig] = deriveReader
  implicit val cassandraDatastaxDriverGroupConfigReader: ConfigReader[GroupConfig] = deriveReader
  implicit val cassandraDatastaxDriverShutdownConfigReader: ConfigReader[ShutdownConfig] = deriveReader
  implicit val cassandraDatastaxDriverTimerConfigReader: ConfigReader[TimerConfig] = deriveReader
  implicit val cassandraDatastaxDriverCoalescerConfigReader: ConfigReader[CoalescerConfig] = deriveReader
  implicit val cassandraDatastaxDriverAdvancedRequestConfigReader: ConfigReader[AdvancedRequestConfig] = deriveReader

  // Profile overridable config
  implicit val cassandraDatastaxDriverProfileConfigReader: ConfigReader[ProfileConfig] = deriveReader
  implicit val cassandraDatastaxDriverProfileBasicConfigReader: ConfigReader[ProfileBasicConfig] = deriveReader
  implicit val cassandraDatastaxDriverProfileAdvancedConfigReader: ConfigReader[ProfileAdvancedConfig] = deriveReader
  implicit val cassandraDatastaxDriverProfilePreparedStatementsConfigReader: ConfigReader[ProfilePreparedStatementsConfig] = deriveReader
  implicit val cassandraDatastaxDriverProfileProfileAdvancedRequestConfigReader: ConfigReader[ProfileAdvancedRequestConfig] = deriveReader
}
