package com.avast.sst.datastax.pureconfig

import com.avast.sst.datastax.config.*
import pureconfig.{ConfigFieldMapping, ConfigReader}
import pureconfig.generic.derivation.default.*

trait ConfigReaders {

  implicit val cassandraDatastaxDriverDatastaxConfigReader: ConfigReader[CassandraDatastaxDriverConfig] =
    ConfigReader.derived

  // Basic driver config
  implicit val cassandraDatastaxDriverBasicConfigReader: ConfigReader[BasicConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverConsistencyLevelReader: ConfigReader[ConsistencyLevel] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverBasicRequestConfigReader: ConfigReader[BasicRequestConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverLoadBalancingConfigReader: ConfigReader[LoadBalancingPolicyConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverFilterConfigReader: ConfigReader[FilterConfig] = ConfigReader.derived

  // Advanced driver config
  implicit val cassandraDatastaxDriverAdvancedConfigReader: ConfigReader[AdvancedConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverRetryPolicyConfigReader: ConfigReader[RetryPolicyConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverSpeculativeExecutionPolicyConfigReader: ConfigReader[SpeculativeExecutionPolicyConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverTimestampGeneratorConfigReader: ConfigReader[TimestampGeneratorConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverTraceConfigReader: ConfigReader[TraceConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverDriftWarningConfigReader: ConfigReader[DriftWarningConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverConnectionConfigReader: ConfigReader[ConnectionConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverPoolConfigReader: ConfigReader[PoolConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverReconnectionPolicyConfigReader: ConfigReader[ReconnectionPolicyConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverAuthProviderConfigReader: ConfigReader[AuthProviderConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverRequestTrackerConfigReader: ConfigReader[RequestTrackerConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverLogsConfigReader: ConfigReader[LogsConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverSlowConfigReader: ConfigReader[SlowConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverThrottlerConfigReader: ConfigReader[ThrottlerConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverNodeStateListenerConfigReader: ConfigReader[NodeStateListenerConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverSchemaChangeListenerConfigReader: ConfigReader[SchemaChangeListenerConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverAddressTranslatorConfigReader: ConfigReader[AddressTranslatorConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverProtocolConfigReader: ConfigReader[ProtocolConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverMetricsConfigReader: ConfigReader[MetricsConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverSessionConfigReader: ConfigReader[SessionConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverMetricsFactoryConfigReader: ConfigReader[MetricsFactoryConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverIdGeneratorConfigReader: ConfigReader[IdGeneratorConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverContinuousRequestsConfigReader: ConfigReader[ContinuousCqlRequests] = ConfigReader.derived
  implicit val cassandraDatastaxDriverGraphRequestsConfigReader: ConfigReader[GraphRequests] = ConfigReader.derived
  implicit val cassandraDatastaxDriverCqlRequestsConfigReader: ConfigReader[CqlRequestsConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverThrottlingConfigReader: ConfigReader[ThrottlingConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverDelayConfigReader: ConfigReader[DelayConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverGraphMessagesConfigReader: ConfigReader[GraphMessagesConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverNodeConfigReader: ConfigReader[NodeConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverCqlMessagesConfigReader: ConfigReader[CqlMessagesConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverSocketConfigReader: ConfigReader[SocketConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverHeartbeatConfigReader: ConfigReader[HeartbeatConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverMetadataConfigReader: ConfigReader[MetadataConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverTopologyEventDebouncerConfigReader: ConfigReader[TopologyEventDebouncerConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverSchemaConfigReader: ConfigReader[SchemaConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverDebouncerConfigReader: ConfigReader[DebouncerConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverTokenMapConfigReader: ConfigReader[TokenMapConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverControlConnectionConfigReader: ConfigReader[ControlConnectionConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverSchemaAgreementConfigReader: ConfigReader[SchemaAgreementConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverPreparedStatementsConfigReader: ConfigReader[PreparedStatementsConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverReprepareOnUpConfigReader: ConfigReader[ReprepareOnUpConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverNettyConfigReader: ConfigReader[NettyConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverGroupConfigReader: ConfigReader[GroupConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverShutdownConfigReader: ConfigReader[ShutdownConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverTimerConfigReader: ConfigReader[TimerConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverCoalescerConfigReader: ConfigReader[CoalescerConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverAdvancedRequestConfigReader: ConfigReader[AdvancedRequestConfig] =
    ConfigReader.derived

  // Profile overridable config
  implicit val cassandraDatastaxDriverProfileConfigReader: ConfigReader[ProfileConfig] = ConfigReader.derived
  implicit val cassandraDatastaxDriverProfileBasicConfigReader: ConfigReader[ProfileBasicConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverProfileAdvancedConfigReader: ConfigReader[ProfileAdvancedConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverProfilePreparedStatementsConfigReader: ConfigReader[ProfilePreparedStatementsConfig] =
    ConfigReader.derived
  implicit val cassandraDatastaxDriverProfileProfileAdvancedRequestConfigReader: ConfigReader[ProfileAdvancedRequestConfig] =
    ConfigReader.derived
}
