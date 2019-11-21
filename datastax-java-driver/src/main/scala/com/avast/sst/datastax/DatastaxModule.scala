package com.avast.sst.datastax

import cats.Traverse
import cats.effect.{Resource, Sync}
import cats.implicits._
import com.avast.sst.datastax.config.DatastaxDriverConfig
import com.avast.sst.datastax.DatastaxHelper._
import com.avast.sst.datastax.ssl.{KeyStoreConfig, KeyStoreType, SslContextConfig, SslContextModule}
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.config.DefaultDriverOption._
import com.datastax.oss.driver.api.core.config.{DriverConfigLoader, ProgrammaticDriverConfigLoaderBuilder => DriverBuilder}
import javax.net.ssl.SSLContext

object DatastaxModule {

  /** Makes [[com.datastax.oss.driver.api.core.CqlSession]] initialized with the given config.
    */
  def make[F[_]: Sync](cfg: DatastaxDriverConfig): Resource[F, CqlSession] = {
    val builder = List[DriverBuilder => DriverBuilder](
      stringListProperty(CONTACT_POINTS)(cfg.basic.contactPoints),
      optional(stringProperty(SESSION_NAME), cfg.basic.sessionName),
      optional(stringProperty(SESSION_KEYSPACE), cfg.basic.sessionKeyspace),
      durationProperty(CONFIG_RELOAD_INTERVAL)(cfg.basic.configReloadInterval),
      durationProperty(REQUEST_TIMEOUT)(cfg.basic.request.timeout),
      stringProperty(REQUEST_CONSISTENCY)(cfg.basic.request.consistency.toStringRepr),
      intProperty(REQUEST_PAGE_SIZE)(cfg.basic.request.pageSize),
      stringProperty(REQUEST_SERIAL_CONSISTENCY)(cfg.basic.request.serialConsistency.toStringRepr),
      booleanProperty(REQUEST_DEFAULT_IDEMPOTENCE)(cfg.basic.request.defaultIdempotence),
      stringProperty(LOAD_BALANCING_POLICY_CLASS)(cfg.basic.loadBalancingPolicy.`class`),
      optional(stringProperty(LOAD_BALANCING_LOCAL_DATACENTER), cfg.basic.loadBalancingPolicy.localDatacenter),
      optional(stringProperty(LOAD_BALANCING_FILTER_CLASS), cfg.basic.loadBalancingPolicy.filter.map(_.`class`)),
      durationProperty(CONNECTION_INIT_QUERY_TIMEOUT)(cfg.advanced.connection.initQueryTimeout),
      intProperty(CONNECTION_POOL_LOCAL_SIZE)(cfg.advanced.connection.localPool.size),
      intProperty(CONNECTION_POOL_REMOTE_SIZE)(cfg.advanced.connection.remotePool.size),
      intProperty(CONNECTION_MAX_REQUESTS)(cfg.advanced.connection.maxRequestsPerConnection),
      intProperty(CONNECTION_MAX_ORPHAN_REQUESTS)(cfg.advanced.connection.maxOrphanRequests),
      booleanProperty(CONNECTION_WARN_INIT_ERROR)(cfg.advanced.connection.warnOnInitError),
      booleanProperty(RECONNECT_ON_INIT)(cfg.advanced.reconnectOnInit),
      stringProperty(RECONNECTION_POLICY_CLASS)(cfg.advanced.reconnectionPolicy.`class`),
      durationProperty(RECONNECTION_BASE_DELAY)(cfg.advanced.reconnectionPolicy.baseDelay),
      optional(durationProperty(RECONNECTION_MAX_DELAY), cfg.advanced.reconnectionPolicy.maxDelay),
      stringProperty(RETRY_POLICY_CLASS)(cfg.advanced.retryPolicy.`class`),
      stringProperty(SPECULATIVE_EXECUTION_POLICY_CLASS)(cfg.advanced.speculativeExecutionPolicy.`class`),
      optional(intProperty(SPECULATIVE_EXECUTION_MAX), cfg.advanced.speculativeExecutionPolicy.maxExecutions),
      optional(durationProperty(SPECULATIVE_EXECUTION_DELAY), cfg.advanced.speculativeExecutionPolicy.delay),
      optional(stringProperty(AUTH_PROVIDER_CLASS), cfg.advanced.authProvider.map(_.`class`)),
      optional(stringProperty(AUTH_PROVIDER_USER_NAME), cfg.advanced.authProvider.map(_.username)),
      optional(stringProperty(AUTH_PROVIDER_PASSWORD), cfg.advanced.authProvider.map(_.password)),
      optional(stringProperty(SSL_ENGINE_FACTORY_CLASS), cfg.advanced.sslEngineFactory.flatMap(_.`class`)),
      optional(stringListProperty(SSL_CIPHER_SUITES), cfg.advanced.sslEngineFactory.map(_.cipherSuites)),
      optional(stringProperty(SSL_KEYSTORE_PATH), cfg.advanced.sslEngineFactory.flatMap(_.keystorePath)),
      optional(stringProperty(SSL_KEYSTORE_PASSWORD), cfg.advanced.sslEngineFactory.flatMap(_.keystorePassword)),
      optional(stringProperty(SSL_TRUSTSTORE_PATH), cfg.advanced.sslEngineFactory.flatMap(_.truststorePath)),
      optional(stringProperty(SSL_TRUSTSTORE_PASSWORD), cfg.advanced.sslEngineFactory.flatMap(_.truststorePassword)),
      optional(booleanProperty(SSL_HOSTNAME_VALIDATION), cfg.advanced.sslEngineFactory.flatMap(_.hostnameValidation)),
      stringProperty(TIMESTAMP_GENERATOR_CLASS)(cfg.advanced.timestampGenerator.`class`),
      durationProperty(TIMESTAMP_GENERATOR_DRIFT_WARNING_THRESHOLD)(cfg.advanced.timestampGenerator.driftWarning.threshold),
      durationProperty(TIMESTAMP_GENERATOR_DRIFT_WARNING_INTERVAL)(cfg.advanced.timestampGenerator.driftWarning.interval),
      booleanProperty(TIMESTAMP_GENERATOR_FORCE_JAVA_CLOCK)(cfg.advanced.timestampGenerator.forceJavaClock),
      stringProperty(REQUEST_TRACKER_CLASS)(cfg.advanced.requestTracker.`class`),
      optional(booleanProperty(REQUEST_LOGGER_SUCCESS_ENABLED), cfg.advanced.requestTracker.logs.flatMap(_.successEnabled)),
      optional(booleanProperty(REQUEST_LOGGER_ERROR_ENABLED), cfg.advanced.requestTracker.logs.flatMap(_.errorEnabled)),
      optional(intProperty(REQUEST_LOGGER_MAX_QUERY_LENGTH), cfg.advanced.requestTracker.logs.flatMap(_.maxQueryLength)),
      optional(booleanProperty(REQUEST_LOGGER_VALUES), cfg.advanced.requestTracker.logs.flatMap(_.showValues)),
      optional(intProperty(REQUEST_LOGGER_MAX_VALUE_LENGTH), cfg.advanced.requestTracker.logs.flatMap(_.maxValueLength)),
      optional(intProperty(REQUEST_LOGGER_MAX_VALUES), cfg.advanced.requestTracker.logs.flatMap(_.maxValues)),
      optional(booleanProperty(REQUEST_LOGGER_STACK_TRACES), cfg.advanced.requestTracker.logs.flatMap(_.showStackTraces)),
      optional(durationProperty(REQUEST_LOGGER_SLOW_THRESHOLD), cfg.advanced.requestTracker.logs.flatMap(_.slow.flatMap(_.threshold))),
      optional(booleanProperty(REQUEST_LOGGER_SLOW_ENABLED), cfg.advanced.requestTracker.logs.flatMap(_.slow.flatMap(_.enabled))),
      stringProperty(REQUEST_THROTTLER_CLASS)(cfg.advanced.throttler.`class`),
      optional(intProperty(REQUEST_THROTTLER_MAX_QUEUE_SIZE), cfg.advanced.throttler.maxQueueSize),
      optional(intProperty(REQUEST_THROTTLER_MAX_CONCURRENT_REQUESTS), cfg.advanced.throttler.maxConcurrentRequests),
      optional(intProperty(REQUEST_THROTTLER_MAX_REQUESTS_PER_SECOND), cfg.advanced.throttler.maxRequestsPerSecond),
      optional(durationProperty(REQUEST_THROTTLER_DRAIN_INTERVAL), cfg.advanced.throttler.drainInterval),
      stringProperty(METADATA_NODE_STATE_LISTENER_CLASS)(cfg.advanced.nodeStateListener.`class`),
      stringProperty(METADATA_SCHEMA_CHANGE_LISTENER_CLASS)(cfg.advanced.schemaChangeListener.`class`),
      stringProperty(ADDRESS_TRANSLATOR_CLASS)(cfg.advanced.addressTranslator.`class`),
      booleanProperty(RESOLVE_CONTACT_POINTS)(cfg.advanced.resolveContactPoints),
      optional(stringProperty(PROTOCOL_VERSION), cfg.advanced.protocol.version),
      optional(stringProperty(PROTOCOL_COMPRESSION), cfg.advanced.protocol.compression),
      intProperty(PROTOCOL_MAX_FRAME_LENGTH)(cfg.advanced.protocol.maxFrameLength),
      booleanProperty(REQUEST_WARN_IF_SET_KEYSPACE)(cfg.advanced.request.warnIfSetKeyspace),
      intProperty(REQUEST_TRACE_ATTEMPTS)(cfg.advanced.request.trace.attempts),
      durationProperty(REQUEST_TRACE_INTERVAL)(cfg.advanced.request.trace.interval),
      stringProperty(REQUEST_TRACE_CONSISTENCY)(cfg.advanced.request.trace.consistency.toStringRepr),
      booleanProperty(REQUEST_LOG_WARNINGS)(cfg.advanced.request.logWarnings),
      optional(intListProperty(METRICS_SESSION_ENABLED), cfg.advanced.metrics.session.map(_.enabled)),
      optional(durationProperty(METRICS_SESSION_CQL_REQUESTS_HIGHEST),
               cfg.advanced.metrics.session.flatMap(_.cqlRequests.map(_.highestLatency))),
      optional(durationProperty(METRICS_SESSION_CQL_REQUESTS_INTERVAL),
               cfg.advanced.metrics.session.flatMap(_.cqlRequests.map(_.refreshInterval))),
      optional(intProperty(METRICS_SESSION_CQL_REQUESTS_DIGITS),
               cfg.advanced.metrics.session.flatMap(_.cqlRequests.map(_.significantDigits))),
      optional(durationProperty(METRICS_SESSION_THROTTLING_HIGHEST),
               cfg.advanced.metrics.session.flatMap(_.throttling.flatMap(_.delay.map(_.highestLatency)))),
      optional(durationProperty(METRICS_SESSION_THROTTLING_INTERVAL),
               cfg.advanced.metrics.session.flatMap(_.throttling.flatMap(_.delay.map(_.refreshInterval)))),
      optional(intProperty(METRICS_SESSION_THROTTLING_DIGITS),
               cfg.advanced.metrics.session.flatMap(_.throttling.flatMap(_.delay.map(_.significantDigits)))),
      optional(intListProperty(METRICS_NODE_ENABLED), cfg.advanced.metrics.node.map(_.enabled)),
      optional(durationProperty(METRICS_NODE_CQL_MESSAGES_HIGHEST), cfg.advanced.metrics.node.flatMap(_.cqlRequests.map(_.highestLatency))),
      optional(intProperty(METRICS_NODE_CQL_MESSAGES_DIGITS), cfg.advanced.metrics.node.flatMap(_.cqlRequests.map(_.significantDigits))),
      optional(durationProperty(METRICS_NODE_CQL_MESSAGES_INTERVAL),
               cfg.advanced.metrics.node.flatMap(_.cqlRequests.map(_.refreshInterval))),
      durationProperty(HEARTBEAT_INTERVAL)(cfg.advanced.heartbeat.interval),
      durationProperty(HEARTBEAT_TIMEOUT)(cfg.advanced.heartbeat.timeout),
      booleanProperty(SOCKET_TCP_NODELAY)(cfg.advanced.socket.tcpNoDelay),
      optional(booleanProperty(SOCKET_KEEP_ALIVE), cfg.advanced.socket.keepAlive),
      optional(booleanProperty(SOCKET_REUSE_ADDRESS), cfg.advanced.socket.reuseAddress),
      optional(intProperty(SOCKET_LINGER_INTERVAL), cfg.advanced.socket.lingerInterval),
      optional(intProperty(SOCKET_RECEIVE_BUFFER_SIZE), cfg.advanced.socket.receiveBufferSize),
      optional(intProperty(SOCKET_SEND_BUFFER_SIZE), cfg.advanced.socket.sendBufferSize),
      durationProperty(METADATA_TOPOLOGY_WINDOW)(cfg.advanced.metadata.debouncer.window),
      intProperty(METADATA_TOPOLOGY_MAX_EVENTS)(cfg.advanced.metadata.debouncer.maxEvents),
      booleanProperty(METADATA_SCHEMA_ENABLED)(cfg.advanced.metadata.schema.enabled),
      stringListProperty(METADATA_SCHEMA_REFRESHED_KEYSPACES)(cfg.advanced.metadata.schema.refreshedKeyspaces),
      durationProperty(METADATA_SCHEMA_REQUEST_TIMEOUT)(cfg.advanced.metadata.schema.requestTimeout),
      intProperty(METADATA_SCHEMA_REQUEST_PAGE_SIZE)(cfg.advanced.metadata.schema.requestPageSize),
      durationProperty(METADATA_SCHEMA_WINDOW)(cfg.advanced.metadata.schema.debouncer.window),
      intProperty(METADATA_SCHEMA_MAX_EVENTS)(cfg.advanced.metadata.schema.debouncer.maxEvents),
      booleanProperty(METADATA_TOKEN_MAP_ENABLED)(cfg.advanced.metadata.tokenMap.enabled),
      durationProperty(CONTROL_CONNECTION_TIMEOUT)(cfg.advanced.controlConnection.timeout),
      durationProperty(CONTROL_CONNECTION_AGREEMENT_INTERVAL)(cfg.advanced.controlConnection.schemaAgreement.interval),
      durationProperty(CONTROL_CONNECTION_AGREEMENT_TIMEOUT)(cfg.advanced.controlConnection.schemaAgreement.timeout),
      booleanProperty(CONTROL_CONNECTION_AGREEMENT_WARN)(cfg.advanced.controlConnection.schemaAgreement.warnOnFailure),
      booleanProperty(PREPARE_ON_ALL_NODES)(cfg.advanced.preparedStatements.prepareOnAllNodes),
      booleanProperty(REPREPARE_ENABLED)(cfg.advanced.preparedStatements.reprepareOnUp.enabled),
      booleanProperty(REPREPARE_CHECK_SYSTEM_TABLE)(cfg.advanced.preparedStatements.reprepareOnUp.checkSystemTable),
      intProperty(REPREPARE_MAX_STATEMENTS)(cfg.advanced.preparedStatements.reprepareOnUp.maxStatements),
      intProperty(REPREPARE_MAX_PARALLELISM)(cfg.advanced.preparedStatements.reprepareOnUp.maxParallelism),
      durationProperty(REPREPARE_TIMEOUT)(cfg.advanced.preparedStatements.reprepareOnUp.timeout),
      booleanProperty(NETTY_DAEMON)(cfg.advanced.netty.daemon),
      intProperty(NETTY_IO_SIZE)(cfg.advanced.netty.ioGroup.size),
      intProperty(NETTY_IO_SHUTDOWN_TIMEOUT)(cfg.advanced.netty.ioGroup.shutdown.timeout),
      intProperty(NETTY_IO_SHUTDOWN_QUIET_PERIOD)(cfg.advanced.netty.ioGroup.shutdown.quietPeriod),
      stringProperty(NETTY_IO_SHUTDOWN_UNIT)(cfg.advanced.netty.ioGroup.shutdown.unit),
      intProperty(NETTY_ADMIN_SIZE)(cfg.advanced.netty.adminGroup.size),
      intProperty(NETTY_ADMIN_SHUTDOWN_TIMEOUT)(cfg.advanced.netty.adminGroup.shutdown.timeout),
      intProperty(NETTY_ADMIN_SHUTDOWN_QUIET_PERIOD)(cfg.advanced.netty.adminGroup.shutdown.quietPeriod),
      stringProperty(NETTY_ADMIN_SHUTDOWN_UNIT)(cfg.advanced.netty.adminGroup.shutdown.unit),
      durationProperty(NETTY_TIMER_TICK_DURATION)(cfg.advanced.netty.timer.tickDuration),
      intProperty(NETTY_TIMER_TICKS_PER_WHEEL)(cfg.advanced.netty.timer.ticksPerWheel),
      intProperty(COALESCER_MAX_RUNS)(cfg.advanced.coalescer.maxRunsWithNoWork)
    ).foldRight(DriverConfigLoader.programmaticBuilder()) { (w, b) =>
      w(b)
    }

    val loader = cfg
      .profiles
      .foldRight(builder) { (p, b) =>
        List[DriverBuilder => DriverBuilder](
          durationProperty(REQUEST_TIMEOUT)(p.basic.request.timeout),
          stringProperty(REQUEST_CONSISTENCY)(p.basic.request.consistency.toStringRepr),
          intProperty(REQUEST_PAGE_SIZE)(p.basic.request.pageSize),
          stringProperty(REQUEST_SERIAL_CONSISTENCY)(p.basic.request.serialConsistency.toStringRepr),
          booleanProperty(REQUEST_DEFAULT_IDEMPOTENCE)(p.basic.request.defaultIdempotence),
          intProperty(REQUEST_TRACE_ATTEMPTS)(p.advanced.request.trace.attempts),
          durationProperty(REQUEST_TRACE_INTERVAL)(p.advanced.request.trace.interval),
          stringProperty(REQUEST_TRACE_CONSISTENCY)(p.advanced.request.trace.consistency.toStringRepr),
          booleanProperty(REQUEST_LOG_WARNINGS)(p.advanced.request.logWarnings)
        ).foldRight(b.startProfile(p.name)) { (w, pb) =>
            w(pb)
          }
          .endProfile()
      }
      .build()

    // Construct and pass SSL context, because parameter `advanced.ssl-engine-factory` is ignored
    val sslConfig = cfg.advanced.sslEngineFactory.map { cfg =>
      val ts = for {
        path <- cfg.truststorePath.map(java.nio.file.Paths.get(_))
        pass <- cfg.truststorePassword
      } yield KeyStoreConfig(KeyStoreType.JKS, path, pass, None)

      val ks = for {
        path <- cfg.keystorePath.map(java.nio.file.Paths.get(_))
        pass <- cfg.keystorePassword
      } yield KeyStoreConfig(KeyStoreType.JKS, path, pass, None)

      SslContextConfig(keystore = ks, truststore = ts)
    }

    val acquire = Sync[F].map(sslContext(sslConfig)) {
      case Some(ssl) => CqlSession.builder().withConfigLoader(loader).withSslContext(ssl).build()
      case None      => CqlSession.builder().withConfigLoader(loader).build()
    }

    val release = { session: CqlSession =>
      Sync[F].delay {
        session.close()
      }
    }

    Resource.make(acquire)(release)
  }

  private def sslContext[F[_]: Sync](maybeSslContextConfig: Option[SslContextConfig]): F[Option[SSLContext]] = {
    Traverse[Option].traverse(maybeSslContextConfig)(SslContextModule.make[F])
  }
}
