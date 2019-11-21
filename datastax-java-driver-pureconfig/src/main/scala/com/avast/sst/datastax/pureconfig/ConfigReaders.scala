package com.avast.sst.datastax.pureconfig

import com.avast.sst.datastax.config._
import pureconfig.{ConfigFieldMapping, ConfigReader, PascalCase}
import pureconfig.generic.semiauto.{deriveEnumerationReader, deriveReader}

trait ConfigReaders {

  implicit val datastaxJavaDriverDatastaxConfigReader: ConfigReader[DatastaxDriverConfig] = deriveReader

  // Basic driver config
  implicit val datastaxJavaDriverBasicConfigReader: ConfigReader[Basic] = deriveReader
  implicit val datastaxJavaDriverConsistencyLevelReader: ConfigReader[ConsistencyLevel] =
    deriveEnumerationReader[ConsistencyLevel](ConfigFieldMapping(PascalCase, PascalCase))
  implicit val datastaxJavaDriverBasicRequestConfigReader: ConfigReader[BasicRequest] = deriveReader
  implicit val datastaxJavaDriverLoadBalancingConfigReader: ConfigReader[LoadBalancingPolicy] = deriveReader
  implicit val datastaxJavaDriverFilterConfigReader: ConfigReader[Filter] = deriveReader

  // Advanced driver config
  implicit val datastaxJavaDriverAdvancedConfigReader: ConfigReader[Advanced] = deriveReader
  implicit val datastaxJavaDriverRetryPolicyConfigReader: ConfigReader[RetryPolicy] = deriveReader
  implicit val datastaxJavaDriverSpeculativeExecutionPolicyConfigReader: ConfigReader[SpeculativeExecutionPolicy] = deriveReader
  implicit val datastaxJavaDriverTimestampGeneratorConfigReader: ConfigReader[TimestampGenerator] = deriveReader
  implicit val datastaxJavaDriverTraceConfigReader: ConfigReader[Trace] = deriveReader
  implicit val datastaxJavaDriverDriftWarningConfigReader: ConfigReader[DriftWarning] = deriveReader
  implicit val datastaxJavaDriverConnectionConfigReader: ConfigReader[Connection] = deriveReader
  implicit val datastaxJavaDriverPoolConfigReader: ConfigReader[Pool] = deriveReader
  implicit val datastaxJavaDriverReconnectionPolicyConfigReader: ConfigReader[ReconnectionPolicy] = deriveReader
  implicit val datastaxJavaDriverAuthProviderConfigReader: ConfigReader[AuthProvider] = deriveReader
  implicit val datastaxJavaDriverSslEngineFactoryConfigReader: ConfigReader[SslEngineFactory] = deriveReader
  implicit val datastaxJavaDriverRequestTrackerConfigReader: ConfigReader[RequestTracker] = deriveReader
  implicit val datastaxJavaDriverLogsConfigReader: ConfigReader[Logs] = deriveReader
  implicit val datastaxJavaDriverSlowConfigReader: ConfigReader[Slow] = deriveReader
  implicit val datastaxJavaDriverThrottlerConfigReader: ConfigReader[Throttler] = deriveReader
  implicit val datastaxJavaDriverNodeStateListenerConfigReader: ConfigReader[NodeStateListener] = deriveReader
  implicit val datastaxJavaDriverSchemaChangeListenerConfigReader: ConfigReader[SchemaChangeListener] = deriveReader
  implicit val datastaxJavaDriverAddressTranslatorConfigReader: ConfigReader[AddressTranslator] = deriveReader
  implicit val datastaxJavaDriverProtocolConfigReader: ConfigReader[Protocol] = deriveReader
  implicit val datastaxJavaDriverMetricsConfigReader: ConfigReader[Metrics] = deriveReader
  implicit val datastaxJavaDriverSessionConfigReader: ConfigReader[Session] = deriveReader
  implicit val datastaxJavaDriverCqlRequestsConfigReader: ConfigReader[CqlRequests] = deriveReader
  implicit val datastaxJavaDriverThrottlingConfigReader: ConfigReader[Throttling] = deriveReader
  implicit val datastaxJavaDriverDelayConfigReader: ConfigReader[Delay] = deriveReader
  implicit val datastaxJavaDriverNodeConfigReader: ConfigReader[Node] = deriveReader
  implicit val datastaxJavaDriverCqlMessagesConfigReader: ConfigReader[CqlMessages] = deriveReader
  implicit val datastaxJavaDriverSocketConfigReader: ConfigReader[Socket] = deriveReader
  implicit val datastaxJavaDriverHeartbeatConfigReader: ConfigReader[Heartbeat] = deriveReader
  implicit val datastaxJavaDriverMetadataConfigReader: ConfigReader[Metadata] = deriveReader
  implicit val datastaxJavaDriverTopologyEventDebouncerConfigReader: ConfigReader[TopologyEventDebouncer] = deriveReader
  implicit val datastaxJavaDriverSchemaConfigReader: ConfigReader[Schema] = deriveReader
  implicit val datastaxJavaDriverDebouncerConfigReader: ConfigReader[Debouncer] = deriveReader
  implicit val datastaxJavaDriverTokenMapConfigReader: ConfigReader[TokenMap] = deriveReader
  implicit val datastaxJavaDriverControlConnectionConfigReader: ConfigReader[ControlConnection] = deriveReader
  implicit val datastaxJavaDriverSchemaAgreementConfigReader: ConfigReader[SchemaAgreement] = deriveReader
  implicit val datastaxJavaDriverPreparedStatementsConfigReader: ConfigReader[PreparedStatements] = deriveReader
  implicit val datastaxJavaDriverReprepareOnUpConfigReader: ConfigReader[ReprepareOnUp] = deriveReader
  implicit val datastaxJavaDriverNettyConfigReader: ConfigReader[Netty] = deriveReader
  implicit val datastaxJavaDriverGroupConfigReader: ConfigReader[Group] = deriveReader
  implicit val datastaxJavaDriverShutdownConfigReader: ConfigReader[Shutdown] = deriveReader
  implicit val datastaxJavaDriverTimerConfigReader: ConfigReader[Timer] = deriveReader
  implicit val datastaxJavaDriverCoalescerConfigReader: ConfigReader[Coalescer] = deriveReader
  implicit val datastaxJavaDriverAdvancedRequestConfigReader: ConfigReader[AdvancedRequest] = deriveReader

  // Profile overridable config
  implicit val datastaxJavaDriverProfileConfigReader: ConfigReader[Profile] = deriveReader
  implicit val datastaxJavaDriverProfileBasicConfigReader: ConfigReader[ProfileBasic] = deriveReader
  implicit val datastaxJavaDriverProfileAdvancedConfigReader: ConfigReader[ProfileAdvanced] = deriveReader
  implicit val datastaxJavaDriverProfilePreparedStatementsConfigReader: ConfigReader[ProfilePreparedStatements] = deriveReader
  implicit val datastaxJavaDriverProfileProfileAdvancedRequestConfigReader: ConfigReader[ProfileAdvancedRequest] = deriveReader
}
