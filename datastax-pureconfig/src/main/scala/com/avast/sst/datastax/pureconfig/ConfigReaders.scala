package com.avast.sst.datastax.pureconfig

import com.avast.sst.datastax.config._
import pureconfig.ConfigReader
import pureconfig.generic.semiauto.deriveReader

trait ConfigReaders {

  implicit val datastaxConfigReader: ConfigReader[DatastaxDriverConfig] = deriveReader

  // Basic driver config
  implicit val basicConfigReader: ConfigReader[Basic] = deriveReader
  implicit val basicRequestConfigReader: ConfigReader[BasicRequest] = deriveReader
  implicit val loadBalancingConfigReader: ConfigReader[LoadBalancingPolicy] = deriveReader
  implicit val filterConfigReader: ConfigReader[Filter] = deriveReader

  // Advanced driver config
  implicit val advancedConfigReader: ConfigReader[Advanced] = deriveReader
  implicit val retryPolicyConfigReader: ConfigReader[RetryPolicy] = deriveReader
  implicit val speculativeExecutionPolicyConfigReader: ConfigReader[SpeculativeExecutionPolicy] = deriveReader
  implicit val timestampGeneratorConfigReader: ConfigReader[TimestampGenerator] = deriveReader
  implicit val traceConfigReader: ConfigReader[Trace] = deriveReader
  implicit val driftWarningConfigReader: ConfigReader[DriftWarning] = deriveReader
  implicit val connectionConfigReader: ConfigReader[Connection] = deriveReader
  implicit val poolConfigReader: ConfigReader[Pool] = deriveReader
  implicit val reconnectionPolicyConfigReader: ConfigReader[ReconnectionPolicy] = deriveReader
  implicit val authProviderConfigReader: ConfigReader[AuthProvider] = deriveReader
  implicit val sslEngineFactoryConfigReader: ConfigReader[SslEngineFactory] = deriveReader
  implicit val requestTrackerConfigReader: ConfigReader[RequestTracker] = deriveReader
  implicit val logsConfigReader: ConfigReader[Logs] = deriveReader
  implicit val slowConfigReader: ConfigReader[Slow] = deriveReader
  implicit val throttlerConfigReader: ConfigReader[Throttler] = deriveReader
  implicit val nodeStateListenerConfigReader: ConfigReader[NodeStateListener] = deriveReader
  implicit val schemaChangeListenerConfigReader: ConfigReader[SchemaChangeListener] = deriveReader
  implicit val addressTranslatorConfigReader: ConfigReader[AddressTranslator] = deriveReader
  implicit val protocolConfigReader: ConfigReader[Protocol] = deriveReader
  implicit val metricsConfigReader: ConfigReader[Metrics] = deriveReader
  implicit val sessionConfigReader: ConfigReader[Session] = deriveReader
  implicit val cqlRequestsConfigReader: ConfigReader[CqlRequests] = deriveReader
  implicit val throttlingConfigReader: ConfigReader[Throttling] = deriveReader
  implicit val delayConfigReader: ConfigReader[Delay] = deriveReader
  implicit val nodeConfigReader: ConfigReader[Node] = deriveReader
  implicit val cqlMessagesConfigReader: ConfigReader[CqlMessages] = deriveReader
  implicit val socketConfigReader: ConfigReader[Socket] = deriveReader
  implicit val heartbeatConfigReader: ConfigReader[Heartbeat] = deriveReader
  implicit val metadataConfigReader: ConfigReader[Metadata] = deriveReader
  implicit val topologyEventDebouncerConfigReader: ConfigReader[TopologyEventDebouncer] = deriveReader
  implicit val schemaConfigReader: ConfigReader[Schema] = deriveReader
  implicit val debouncerConfigReader: ConfigReader[Debouncer] = deriveReader
  implicit val tokenMapConfigReader: ConfigReader[TokenMap] = deriveReader
  implicit val controlConnectionConfigReader: ConfigReader[ControlConnection] = deriveReader
  implicit val schemaAgreementConfigReader: ConfigReader[SchemaAgreement] = deriveReader
  implicit val preparedStatementsConfigReader: ConfigReader[PreparedStatements] = deriveReader
  implicit val reprepareOnUpConfigReader: ConfigReader[ReprepareOnUp] = deriveReader
  implicit val nettyConfigReader: ConfigReader[Netty] = deriveReader
  implicit val ioGroupConfigReader: ConfigReader[IoGroup] = deriveReader
  implicit val adminGroupConfigReader: ConfigReader[AdminGroup] = deriveReader
  implicit val shutdownConfigReader: ConfigReader[Shutdown] = deriveReader
  implicit val timerConfigReader: ConfigReader[Timer] = deriveReader
  implicit val coalescerConfigReader: ConfigReader[Coalescer] = deriveReader
  implicit val advancedRequestConfigReader: ConfigReader[AdvancedRequest] = deriveReader

  // Profile overridable config
  implicit val profileConfigReader: ConfigReader[Profile] = deriveReader
  implicit val profileBasicConfigReader: ConfigReader[ProfileBasic] = deriveReader
  implicit val profileProfileBasicRequestConfigReader: ConfigReader[ProfileBasicRequest] = deriveReader
  implicit val profileAdvancedConfigReader: ConfigReader[ProfileAdvanced] = deriveReader
  implicit val profilePreparedStatementsConfigReader: ConfigReader[ProfilePreparedStatements] = deriveReader
  implicit val profileProfileTraceConfigReader: ConfigReader[ProfileTrace] = deriveReader
  implicit val profileProfileAdvancedRequestConfigReader: ConfigReader[ProfileAdvancedRequest] = deriveReader
}
