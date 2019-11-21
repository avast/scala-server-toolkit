package com.avast.sst.datastax.config

import scala.concurrent.duration._

/** Advanced datastax driver configuration
  *
  * @param connection                 Configures query connection.
  * @param reconnectOnInit            Whether to schedule reconnection attempts if all contact points are unreachable on the first
  *                                   initialization attempt.
  *
  *                                   If this is true, the driver will retry according to the reconnection policy. The
  *                                   `SessionBuilder.build()` call -- or the future returned by `SessionBuilder.buildAsync()` --
  *                                   won't complete until a contact point has been reached.
  *
  *                                   If this is false and no contact points are available, the driver will fail.
  * @param reconnectionPolicy         The policy that controls how often the driver tries to re-establish connections to down nodes.
  * @param retryPolicy                The policy that controls if the driver retries requests that have failed on one node.
  * @param speculativeExecutionPolicy The policy that controls if the driver pre-emptively tries other nodes if a node takes too long
  *                                   to respond.
  * @param authProvider               if `None` no authentication will occur.
  * @param sslEngineFactory           if `None` SSL won't be activated.
  * @param timestampGenerator         The generator that assigns a microsecond timestamp to each request.
  * @param requestTracker             A session-wide component that tracks the outcome of requests.
  *                                   By default `com.datastax.oss.driver.internal.core.trackerNoopRequestTracker` is used.
  * @param throttler                  A session-wide component that controls the rate at which requests are executed.
  *                                   By default `PassThroughRequestThrottler` is used.
  * @param nodeStateListener          A session-wide component that listens for node state changes.
  *                                   By default `com.datastax.oss.driver.internal.core.metadata.NoopNodeStateListener` is used.
  * @param schemaChangeListener       A session-wide component that listens for node state changes.
  *                                   By default `com.datastax.oss.driver.internal.core.metadata.schema.NoopSchemaChangeListener` is used.
  * @param addressTranslator          The address translator to use to convert the addresses sent by Cassandra nodes into ones that
  *                                   the driver uses to connect.
  *                                   By default `com.datastax.oss.driver.internal.core.addresstranslation.PassThroughAddressTranslator` is used.
  * @param resolveContactPoints       Whether to resolve the addresses passed to `Basic.contactPoints`.
  *
  *                                   If this is true, addresses are created with `InetSocketAddress(String, int)`: the host name will
  *                                   be resolved the first time, and the driver will use the resolved IP address for all subsequent
  *                                   connection attempts.
  *
  *                                   If this is false, addresses are created with `InetSocketAddress.createUnresolved()`: the host
  *                                   name will be resolved again every time the driver opens a new connection. This is useful for
  *                                   containerized environments where DNS records are more likely to change over time (note that the
  *                                   JVM and OS have their own DNS caching mechanisms, so you might need additional configuration
  *                                   beyond the driver).
  * @param protocol                   The native protocol to use which defines the format of the binary messages between driver and
  *                                   Cassandra.
  * @param request                    Request configuration.
  * @param metrics                    Metrics configuration.
  *                                   Disabled by default.
  * @param heartbeat                  Heartbeat configuration to check if node is alive.
  * @param socket                     Socket configuration.
  * @param metadata                   Metadata about the Cassandra cluster.
  * @param controlConnection          Configures dedicated administrative connection.
  * @param preparedStatements         Prepared statements configuration.
  * @param netty                      Netty configuration which is used internally by driver.
  * @param coalescer                  The component that coalesces writes on the connections.
  */
final case class Advanced(connection: Connection = Advanced.Default.connection,
                          reconnectOnInit: Boolean = Advanced.Default.reconnectOnInit,
                          reconnectionPolicy: ReconnectionPolicy = Advanced.Default.reconnectionPolicy,
                          retryPolicy: RetryPolicy = Advanced.Default.retryPolicy,
                          speculativeExecutionPolicy: SpeculativeExecutionPolicy = Advanced.Default.speculativeExecutionPolicy,
                          authProvider: Option[AuthProvider] = Advanced.Default.authProvider,
                          sslEngineFactory: Option[SslEngineFactory] = Advanced.Default.sslEngineFactory,
                          timestampGenerator: TimestampGenerator = Advanced.Default.timestampGenerator,
                          requestTracker: RequestTracker = Advanced.Default.requestTracker,
                          throttler: Throttler = Advanced.Default.throttler,
                          nodeStateListener: NodeStateListener = Advanced.Default.nodeStateListener,
                          schemaChangeListener: SchemaChangeListener = Advanced.Default.schemaChangeListener,
                          addressTranslator: AddressTranslator = Advanced.Default.addressTranslator,
                          resolveContactPoints: Boolean = Advanced.Default.resolveContactPoints,
                          protocol: Protocol = Advanced.Default.protocol,
                          request: AdvancedRequest = Advanced.Default.request,
                          metrics: Metrics = Advanced.Default.metrics,
                          heartbeat: Heartbeat = Advanced.Default.heartbeat,
                          socket: Socket = Advanced.Default.socket,
                          metadata: Metadata = Advanced.Default.metadata,
                          controlConnection: ControlConnection = Advanced.Default.controlConnection,
                          preparedStatements: PreparedStatements = Advanced.Default.preparedStatements,
                          netty: Netty = Advanced.Default.netty,
                          coalescer: Coalescer = Advanced.Default.coalescer)

object Advanced {
  val Default: Advanced = Advanced(
    Connection.Default,
    false,
    ReconnectionPolicy.Default,
    RetryPolicy.Default,
    SpeculativeExecutionPolicy.Default,
    None,
    None,
    TimestampGenerator.Default,
    RequestTracker.Default,
    Throttler.Default,
    NodeStateListener.Default,
    SchemaChangeListener.Default,
    AddressTranslator.Default,
    true,
    Protocol.Default,
    AdvancedRequest.Default,
    Metrics.Default,
    Heartbeat.Default,
    Socket.Default,
    Metadata.Default,
    ControlConnection.Default,
    PreparedStatements.Default,
    Netty.Default,
    Coalescer.Default
  )
}

/**
  *
  * @param warnIfSetKeyspace Whether a warning is logged when a request (such as a CQL `USE ...`) changes the active
  *                          keyspace.
  * @param logWarnings       Whether logging of server warnings generated during query execution should be disabled by the
  *                          driver.
  */
final case class AdvancedRequest(warnIfSetKeyspace: Boolean, trace: Trace, logWarnings: Boolean)

object AdvancedRequest {
  val Default: AdvancedRequest = AdvancedRequest(true, Trace.Default, true)
}

/** Configure query connection properties.
  *
  * @param initQueryTimeout         The timeout to use for internal queries that run as part of the initialization process. If
  *                                 this timeout fires, the initialization of the connection will fail. If this is the first
  *                                 connection ever, the driver will fail to initialize as well, otherwise it will retry the
  *                                 connection later.
  * @param setKeyspaceTimeout       The timeout to use when the driver changes the keyspace on a connection at runtime.
  * @param localPool                The driver maintains a connection pool to each node
  * @param remotePool               The driver maintains a connection pool to each node
  * @param maxRequestsPerConnection The maximum number of requests that can be executed concurrently on a connection.
  *                                 This must be between 1 and 32768.
  * @param maxOrphanRequests        The maximum number of "orphaned" requests before a connection gets closed automatically.
  * @param warnOnInitError          Whether to log non-fatal errors when the driver tries to open a new connection.
  */
final case class Connection(initQueryTimeout: Duration = Connection.Default.initQueryTimeout,
                            setKeyspaceTimeout: Duration = Connection.Default.setKeyspaceTimeout,
                            localPool: Pool = Connection.Default.localPool,
                            remotePool: Pool = Connection.Default.remotePool,
                            maxRequestsPerConnection: Int = Connection.Default.maxRequestsPerConnection,
                            maxOrphanRequests: Int = Connection.Default.maxOrphanRequests,
                            warnOnInitError: Boolean = Connection.Default.warnOnInitError)

object Connection {
  val Default: Connection = Connection(InitQueryTimeout, InitQueryTimeout, Pool.Default, Pool.Default, 1024, 24576, true)
}

/** The driver maintains a connection pool to each node, according to the distance assigned to it
  * by the load balancing policy
  *
  * @param size The number of connections in the pool
  */
final case class Pool(size: Int)

object Pool {
  val Default: Pool = Pool(1)
}

/** The policy that controls how often the driver tries to re-establish connections to down nodes.
  *
  * @param class      The class of the policy. If it is not qualified, the driver assumes that it resides in the
  *                   package `com.datastax.oss.driver.internal.core.connection`.
  * @param baseDelay  Reconnection policy starts with the base delay.
  * @param maxDelay   Reconnection policy increases delay up to the max delay.
  */
final case class ReconnectionPolicy(`class`: String = ReconnectionPolicy.Default.`class`,
                                    baseDelay: Duration = ReconnectionPolicy.Default.baseDelay,
                                    maxDelay: Option[Duration] = ReconnectionPolicy.Default.maxDelay)
object ReconnectionPolicy {

  /** A reconnection policy that waits a constant time between each reconnection attempt. */
  val Constant: ReconnectionPolicy =
    ReconnectionPolicy("com.datastax.oss.driver.internal.core.connection.ConstantReconnectionPolicy", 1.second, None)

  /** A reconnection policy that waits exponentially longer between each reconnection attempt (but keeps a constant delay once a maximum delay is reached). */
  val Exponential: ReconnectionPolicy =
    ReconnectionPolicy("com.datastax.oss.driver.internal.core.connection.ExponentialReconnectionPolicy", 1.second, Some(60.seconds))

  val Default: ReconnectionPolicy = Exponential
}

/** The policy that controls if the driver retries requests that have failed on one node
  *
  * @param class The class of the policy. If it is not qualified, the driver assumes that it resides in the
  *              package `com.datastax.oss.driver.internal.core.retry`.
  */
final case class RetryPolicy(`class`: String)

object RetryPolicy {
  val Default: RetryPolicy = RetryPolicy("com.datastax.oss.driver.internal.core.retry.DefaultRetryPolicy")
}

/** The policy that controls if the driver preemptively tries other nodes if a node takes too long to respond.
  *
  * @param class          The class of the policy. If it is not qualified, the driver assumes that it resides in the
  *                       package `com.datastax.oss.driver.internal.core.specex`.
  * @param maxExecutions  The maximum number of executions (including the initial, non-speculative execution).
  *                       This must be at least one.
  * @param delay          The delay between each execution. 0 is allowed, and will result in all executions being sent
  *                       simultaneously when the request starts.
  */
final case class SpeculativeExecutionPolicy(`class`: String, maxExecutions: Option[Int], delay: Option[Duration])

object SpeculativeExecutionPolicy {

  /**  A policy that schedules a configurable number of speculative executions, separated by a fixed delay.*/
  val ConstantSpeculative: SpeculativeExecutionPolicy = SpeculativeExecutionPolicy(
    "com.datastax.oss.driver.internal.core.specex.ConstantSpeculativeExecutionPolicy",
    Some(3),
    Some(3.milliseconds)
  )

  /** A policy that never triggers speculative executions. */
  val NonSpeculative: SpeculativeExecutionPolicy =
    SpeculativeExecutionPolicy("com.datastax.oss.driver.internal.core.specex.NoSpeculativeExecutionPolicy", None, None)

  val Default: SpeculativeExecutionPolicy = NonSpeculative
}

/** The component that handles authentication on each new connection.
  *
  * @param `class` custom class that implements AuthProvider and has a public constructor with a DriverContext argument
  */
final case class AuthProvider(`class`: String, username: String, password: String)

/** The SSL engine factory that will initialize an SSL engine for each new connection to a server.
  *
  * @param class              The class of the factory. If it is not qualified, the driver assumes that it resides in the
  *                           package `com.datastax.oss.driver.internal.core.ssl`.
  *                           If it is absent, SSL won't be activated.
  * @param cipherSuites       The cipher suites to enable when creating an SSLEngine for a connection.
  *                           This property is optional. If it is not present, the driver won't explicitly enable cipher
  *                           suites on the engine, which according to the JDK documentations results in "a minimum quality
  *                           of service".
  * @param hostnameValidation Whether or not to require validation that the hostname of the server certificate's common
  *                           name matches the hostname of the server being connected to.
  * @param truststorePath     The location used to access truststore content.
  * @param truststorePassword The password used to access truststore content.
  * @param keystorePath       The location used to access keystore content.
  * @param keystorePassword   The password used to access keystore content.
  */
final case class SslEngineFactory(`class`: Option[String],
                                  cipherSuites: List[String] = List.empty,
                                  hostnameValidation: Option[Boolean],
                                  truststorePath: Option[String],
                                  truststorePassword: Option[String],
                                  keystorePath: Option[String],
                                  keystorePassword: Option[String])

/** The generator that assigns a microsecond timestamp to each request.
  *
  * @param class          The class of the generator. If it is not qualified, the driver assumes that it resides in the
  *                       package `com.datastax.oss.driver.internal.core.time`.
  * @param driftWarning   configure timestamp drift logging
  * @param forceJavaClock Whether to force the driver to use Java's millisecond-precision system clock.
  *                       If this is false, the driver will try to access the microsecond-precision OS clock via native
  *                       calls (and fallback to the Java one if the native calls fail).
  */
final case class TimestampGenerator(`class`: String = TimestampGenerator.Default.`class`,
                                    driftWarning: DriftWarning = TimestampGenerator.Default.driftWarning,
                                    forceJavaClock: Boolean = TimestampGenerator.Default.forceJavaClock)

object TimestampGenerator {

  /** Timestamps are guaranteed to be unique across all client threads. */
  val Atomic: TimestampGenerator =
    TimestampGenerator("com.datastax.oss.driver.internal.core.time.AtomicTimestampGenerator", DriftWarning.Default, false)

  /** Timestamps are assigned by server. */
  val ServerSide: TimestampGenerator =
    TimestampGenerator("com.datastax.oss.driver.internal.core.time.ServerSideTimestampGenerator", DriftWarning.Default, false)

  /** Timestamps that are guaranteed to be unique within each thread only. */
  val ThreadLocal: TimestampGenerator =
    TimestampGenerator("com.datastax.oss.driver.internal.core.time.ThreadLocalTimestampGenerator", DriftWarning.Default, false)

  val Default: TimestampGenerator = Atomic
}

/** Configure warn logging when timestamp drifts.
  *
  * @param threshold How far in the future timestamps are allowed to drift before the warning is logged.
  *                 If it is undefined or set to 0, warnings are disabled.
  * @param interval How often the warning will be logged if timestamps keep drifting above the threshold.
  */
final case class DriftWarning(threshold: Duration, interval: Duration)

object DriftWarning {
  val Default: DriftWarning = DriftWarning(1.second, 10.seconds)
}

/** A session-wide component that tracks the outcome of requests.
  *
  * @param class The class of the tracker. If it is not qualified, the driver assumes that it resides in the
  *              package `com.datastax.oss.driver.internal.core.tracker`.
  * @param logs  Parameters for RequestLogger
  */
final case class RequestTracker(`class`: String = "com.datastax.oss.driver.internal.core.tracker.NoopRequestTracker", logs: Option[Logs])

object RequestTracker {
  val Default: RequestTracker = RequestTracker("com.datastax.oss.driver.internal.core.tracker.NoopRequestTracker", None)
}

/** Parameters for RequestLogger.
  *
  * @param successEnabled  Whether to log successful requests.
  * @param errorEnabled    Whether to log failed requests.
  * @param slow            Slow requests logging.
  * @param maxQueryLength  The maximum length of the query string in the log message. If it is longer than that, it
  *                        will be truncated.
  * @param showValues      Whether to log bound values in addition to the query string.
  * @param maxValueLength  The maximum length for bound values in the log message. If the formatted representation of a
  *                        value is longer than that, it will be truncated.
  * @param maxValues       The maximum number of bound values to log. If a request has more values, the list of values
  *                        will be truncated.
  * @param showStackTraces Whether to log stack traces for failed queries. If this is disabled, the log will just
  *                        include the exception's string representation (generally the class name and message).
  */
final case class Logs(successEnabled: Option[Boolean],
                      errorEnabled: Option[Boolean],
                      slow: Option[Slow],
                      maxQueryLength: Option[Int],
                      showValues: Option[Boolean],
                      maxValueLength: Option[Int],
                      maxValues: Option[Int],
                      showStackTraces: Option[Boolean])

/** Strategy to classify request as "slow".
  *
  * @param threshold  The threshold to classify a successful request as "slow". If this is unset, all successful
  *                   requests will be considered as normal.
  * @param enabled    Whether to log slow requests.
  */
final case class Slow(threshold: Option[Duration], enabled: Option[Boolean])

/** A session-wide component that controls the rate at which requests are executed.
  *
  * @param class                 The class of the throttler. If it is not qualified, the driver assumes that it resides in
  *                              the package com.datastax.oss.driver.internal.core.session.throttling.
  * @param maxQueueSize          The maximum number of requests that can be enqueued when the throttling threshold is exceeded.
  *                              Beyond that size, requests will fail with a RequestThrottlingException.
  * @param maxConcurrentRequests The maximum number of requests that are allowed to execute in parallel.
  *                              Only used by ConcurrencyLimitingRequestThrottler.
  * @param maxRequestsPerSecond  The maximum allowed request rate.
  *                              Only used by RateLimitingRequestThrottler.
  * @param drainInterval         How often the throttler attempts to dequeue requests.
  *                              Only used by RateLimitingRequestThrottler.
  */
final case class Throttler(`class`: String,
                           maxQueueSize: Option[Int],
                           maxConcurrentRequests: Option[Int],
                           maxRequestsPerSecond: Option[Int],
                           drainInterval: Option[Duration])

object Throttler {
  /** A request throttler that limits the number of concurrent requests. */
  val Concurrency: Throttler = Throttler("com.datastax.oss.driver.internal.core.session.throttling.ConcurrencyLimitingRequestThrottler",
                                         Some(10000),
                                         Some(10000),
                                         None,
                                         None)

  /** A request throttler that does not enforce any kind of limitation: requests are always executed immediately. */
  val PassThrough: Throttler =
    Throttler("com.datastax.oss.driver.internal.core.session.throttling.PassThroughRequestThrottler", None, None, None, None)

  /** A request throttler that limits the rate of requests per second. */
  val RateLimiting: Throttler = Throttler("com.datastax.oss.driver.internal.core.session.throttling.RateLimitingRequestThrottler",
                                          Some(10000),
                                          None,
                                          Some(10000),
                                          Some(10.milliseconds))

  val Default: Throttler = PassThrough
}

/** A session-wide component that listens for node state changes. If it is not qualified, the driver
  * assumes that it resides in the package `com.datastax.oss.driver.internal.core.metadata`.
  *
  * The driver provides a single no-op implementation out of the box: `NoopNodeStateListener`.
  * You can also specify a custom class that implements NodeStateListener and has a public
  * constructor with a DriverContext argument.
  */
final case class NodeStateListener(`class`: String)

object NodeStateListener {

  /** Node state listener implementation with empty methods. */
  val Noop: NodeStateListener = NodeStateListener("com.datastax.oss.driver.internal.core.metadata.NoopNodeStateListener")

  val Default: NodeStateListener = Noop
}

/** A session-wide component that listens for node state changes. If it is not qualified, the driver
  * assumes that it resides in the package `com.datastax.oss.driver.internal.core.metadata.schema`.
  *
  * The driver provides a single no-op implementation out of the box: `NoopSchemaChangeListener`.
  *
  * You can also specify a custom class that implements `SchemaChangeListener` and has a public
  * constructor with a DriverContext argument.
  */
final case class SchemaChangeListener(`class`: String)

object SchemaChangeListener {

  /** Schema change listener implementation with empty methods. */
  val Noop: SchemaChangeListener = SchemaChangeListener("com.datastax.oss.driver.internal.core.metadata.schema.NoopSchemaChangeListener")

  val Default: SchemaChangeListener = Noop
}

/** The address translator to use to convert the addresses sent by Cassandra nodes into ones that
  * the driver uses to connect.
  *
  * This is only needed if the nodes are not directly reachable from the driver (for example, the
  * driver is in a different network region and needs to use a public IP, or it connects through a
  * proxy).
  */
final case class AddressTranslator(`class`: String)

object AddressTranslator {
  val Default: AddressTranslator = AddressTranslator("PassThroughAddressTranslator")
}

/** Protocol for query connection.
  *
  * @param version        The native protocol version to use.
  *                       If this option is absent, the driver looks up the versions of the nodes at startup (by default
  *                       in "system.peers.release_version"), and chooses the highest common protocol version.
  * @param compression    The name of the algorithm used to compress protocol frames.
  * @param maxFrameLength The maximum length of the frames supported by the driver in megabytes. Beyond that limit, requests will
  *                       fail with an exception.
  */
final case class Protocol(version: Option[String], compression: Option[String], maxFrameLength: Int)

object Protocol {
  val Default: Protocol = Protocol(None, None, 268435456)
}

/** Trace configuration
  *
  * @param attempts     How many times the driver will attempt to fetch the query if it is not ready yet.
  * @param interval     The interval between each attempt.
  * @param consistency  The consistency level to use for trace queries.
  */
final case class Trace(attempts: Int, interval: Duration, consistency: ConsistencyLevel)

object Trace {
  val Default: Trace = Trace(5, 3.milliseconds, ConsistencyLevel.LocalOne)
}

/** Metrics configuration
  *
  * @param session  The session-level metrics (all disabled by default).
  * @param node     The node-level metrics (all disabled by default).
  */
final case class Metrics(session: Option[Session], node: Option[Node])

object Metrics {
  val Default: Metrics = Metrics(None, None)
}

/** The session-level metrics (all disabled by default).
  *
  * @param enabled     The session-level metrics (all disabled by default).
  * @param cqlRequests Extra configuration (for the metrics that need it). Required if the 'cql-requests' metric is enabled
  * @param throttling  Configures request throttling metrics..
  */
final case class Session(enabled: List[Int] = List.empty, cqlRequests: Option[CqlRequests], throttling: Option[Throttling])

/** Extra metrics configuration
  *
  * @param highestLatency    The largest latency that we expect to record.
  * @param significantDigits The number of significant decimal digits to which internal structures will maintain
  *                          value resolution and separation (for example, 3 means that recordings up to 1 second
  *                          will be recorded with a resolution of 1 millisecond or better).
  *                          This must be between 0 and 5. If the value is out of range, it defaults to 3 and a
  *                          warning is logged.
  * @param refreshInterval   The interval at which percentile data is refreshed.
  */
final case class CqlRequests(highestLatency: Duration = 3.seconds, significantDigits: Int = 3, refreshInterval: Duration = 5.minutes)

/** How long requests are being throttled
  *
  * @param delay This is the time between the start of the session.execute() call, and the moment when the
  *              throttler allows the request to proceed.
  *              Required: if the 'throttling.delay' metric is enabled
  */
final case class Throttling(delay: Option[Delay])

/** Throttling delay metric. */
final case class Delay(highestLatency: Duration = 3.seconds, significantDigits: Int = 3, refreshInterval: Duration = 5.minutes)

/** Node-level metric.
  *
  * @param enabled node-level metrics
  * @param cqlRequests Required: if the 'cql-messages' metric is enabled
  */
final case class Node(enabled: List[Int], cqlRequests: Option[CqlMessages])

final case class CqlMessages(highestLatency: Duration = 3.seconds, significantDigits: Int = 3, refreshInterval: Duration = 5.minutes)

/** Socket configuration.
  *
  * @param tcpNoDelay         Whether or not to disable the Nagle algorithm.
  *                           By default, this option is set to true (Nagle disabled), because the driver has its own
  *                           internal message coalescing algorithm.
  * @param keepAlive          All other socket options are unset by default. The actual value depends on the underlying
  *                           Netty transport.
  * @param reuseAddress       Whether or not to allow address reuse.
  * @param lingerInterval     Sets the linger interval.
  *                           If the value is zero or greater, then it represents a timeout value, in seconds;
  *                           if the value is negative, it means that this option is disabled.
  * @param receiveBufferSize  Sets a hint to the size of the underlying buffers for incoming network I/O.
  * @param sendBufferSize     Sets a hint to the size of the underlying buffers for outgoing network I/O.
  */
final case class Socket(tcpNoDelay: Boolean,
                        keepAlive: Option[Boolean],
                        reuseAddress: Option[Boolean],
                        lingerInterval: Option[Int],
                        receiveBufferSize: Option[Int],
                        sendBufferSize: Option[Int])

object Socket {
  val Default: Socket = Socket(true, None, None, None, None, None)
}

/** If a connection stays idle for that duration (no reads), the driver sends a dummy message on it to make sure
  * it's still alive. If not, the connection is trashed and replaced.
  *
  * @param interval The heartbeat interval
  * @param timeout  How long the driver waits for the response to a heartbeat. If this timeout fires, the heartbeat is
  *                 considered failed.
  */
final case class Heartbeat(interval: Duration, timeout: Duration)

object Heartbeat {
  val Default: Heartbeat = Heartbeat(30.seconds, InitQueryTimeout)
}

/** Metadata
  *
  * @param debouncer Debouncing to smoothen out oscillations if conflicting events are sent out in short bursts.
  * @param schema    Options relating to schema metadata.
  * @param tokenMap  Whether token metadata (Cluster.getMetadata.getTokenMap) is enabled.
  */
final case class Metadata(debouncer: TopologyEventDebouncer = Metadata.Default.debouncer,
                          schema: Schema = Metadata.Default.schema,
                          tokenMap: TokenMap = Metadata.Default.tokenMap)

object Metadata {
  val Default: Metadata = Metadata(TopologyEventDebouncer.Default, Schema.Default, TokenMap(true))
}

/** The debouncer helps smoothen out oscillations if conflicting events are sent out in short bursts.
  *
  * @param window    How long the driver waits to propagate an event. If another event is received within that time, the
  *                  window is reset and a batch of accumulated events will be delivered.
  *                  Debouncing may be disabled by setting the window to 0 or max-events to 1 (not recommended).
  * @param maxEvents The maximum number of events that can accumulate. If this count is reached, the events are
  *                  delivered immediately and the time window is reset. This avoids holding events indefinitely
  *                  if the window keeps getting reset.
  */
final case class TopologyEventDebouncer(window: Duration = TopologyEventDebouncer.Default.window,
                                        maxEvents: Int = TopologyEventDebouncer.Default.maxEvents)

object TopologyEventDebouncer {
  val Default: TopologyEventDebouncer = TopologyEventDebouncer(1.second, 20)
}

/** Options relating to schema metadata (Cluster.getMetadata.getKeyspaces).
  * This metadata is exposed by the driver for informational purposes, and is also necessary for token-aware routing.
  *
  * @param enabled            Whether schema metadata is enabled.
  *                           If this is false, the schema will remain empty, or to the last known value.
  * @param refreshedKeyspaces The list of keyspaces for which schema and token metadata should be maintained. If this
  *                           property is absent or empty, all existing keyspaces are processed.
  * @param requestTimeout     The timeout for the requests to the schema tables.
  * @param requestPageSize    The page size for the requests to the schema tables.
  * @param debouncer          Protects against bursts of schema updates.
  */
final case class Schema(enabled: Boolean = Schema.Default.enabled,
                        refreshedKeyspaces: List[String] = Schema.Default.refreshedKeyspaces,
                        requestTimeout: Duration = Schema.Default.requestTimeout,
                        requestPageSize: Int = Schema.Default.requestPageSize,
                        debouncer: Debouncer = Schema.Default.debouncer)

object Schema {
  val Default: Schema = Schema(true, List.empty, RequestTimeout, RequestPageSize, Debouncer.Default)
}

/** Protects against bursts of schema updates (for example when a client issues a sequence of DDL queries), by
  * coalescing them into a single update.
  *
  * @param window    How long the driver waits to apply a refresh. If another refresh is requested within that
  *                  time, the window is reset and a single refresh will be triggered when it ends.
  *                  Debouncing may be disabled by setting the window to 0 or max-events to 1 (this is highly
  *                  discouraged for schema refreshes).
  * @param maxEvents The maximum number of refreshes that can accumulate. If this count is reached, a refresh
  *                  is done immediately and the window is reset.
  */
final case class Debouncer(window: Duration = Debouncer.Default.window, maxEvents: Int = Debouncer.Default.maxEvents)

object Debouncer {
  val Default: Debouncer = Debouncer(1.second, 20)
}

/** Whether token metadata (Cluster.getMetadata.getTokenMap) is `enabled`.
  * This metadata is exposed by the driver for informational purposes, and is also necessary for token-aware routing.
  * If this is false, it will remain empty, or to the last known value. Note that its computation
  * requires information about the schema; therefore if schema metadata is disabled or filtered to
  * a subset of keyspaces, the token map will be incomplete, regardless of the value of this property.
  */
final case class TokenMap(enabled: Boolean)

final case class ControlConnection(timeout: Duration, schemaAgreement: SchemaAgreement)

object ControlConnection {
  val Default: ControlConnection = ControlConnection(InitQueryTimeout, SchemaAgreement.Default)
}

/** Due to the distributed nature of Cassandra, schema changes made on one node might not be
  * immediately visible to others. Under certain circumstances, the driver waits until all nodes
  * agree on a common schema version (namely: before a schema refresh, before repreparing all
  * queries on a newly up node, and before completing a successful schema-altering query). To do
  * so, it queries system tables to find out the schema version of all nodes that are currently
  * UP. If all the versions match, the check succeeds, otherwise it is retried periodically, until
  * a given timeout.
  *
  * A schema agreement failure is not fatal, but it might produce unexpected results (for example,
  * getting an "unconfigured table" error for a table that you created right before, just because
  * the two queries went to different coordinators).
  *
  * Note that schema agreement never succeeds in a mixed-version cluster (it would be challenging
  * because the way the schema version is computed varies across server versions); the assumption
  * is that schema updates are unlikely to happen during a rolling upgrade anyway.
  *
  * @param interval      The interval between each attempt.
  * @param timeout       The timeout after which schema agreement fails.
  *                      If this is set to 0, schema agreement is skipped and will always fail.
  * @param warnOnFailure Whether to log a warning if schema agreement fails.
  *                      You might want to change this if you've set the timeout to 0.
  */
final case class SchemaAgreement(interval: Duration = SchemaAgreement.Default.interval,
                                 timeout: Duration = SchemaAgreement.Default.timeout,
                                 warnOnFailure: Boolean = SchemaAgreement.Default.warnOnFailure)

object SchemaAgreement {
  val Default: SchemaAgreement = SchemaAgreement(200.milliseconds, 10.seconds, true)
}

/**
  *
  * @param prepareOnAllNodes Overridable in a profile.
  */
final case class PreparedStatements(prepareOnAllNodes: Boolean = PreparedStatements.Default.prepareOnAllNodes,
                                    reprepareOnUp: ReprepareOnUp = PreparedStatements.Default.reprepareOnUp)

object PreparedStatements {
  val Default: PreparedStatements = PreparedStatements(true, ReprepareOnUp.Default)
}

/** How the driver replicates prepared statements on a node that just came back up or joined the cluster.
  *
  * @param enabled          Whether the driver tries to prepare on new nodes at all.
  *
  *                         The reason why you might want to disable it is to optimize reconnection time when you
  *                         believe nodes often get marked down because of temporary network issues, rather than the
  *                         node really crashing. In that case, the node still has prepared statements in its cache when
  *                         the driver reconnects, so re-preparing is redundant.
  *
  *                         On the other hand, if that assumption turns out to be wrong and the node had really
  *                         restarted, its prepared statement cache is empty (before CASSANDRA-8831), and statements
  *                         need to be re-prepared on the fly the first time they get executed; this causes a
  *                         performance penalty (one extra roundtrip to resend the query to prepare, and another to
  *                         retry the execution).
  * @param checkSystemTable Whether to check `system.prepared_statements` on the target node before repreparing.
  *
  *                         This table exists since CASSANDRA-8831 (merged in 3.10). It stores the statements already
  *                         prepared on the node, and preserves them across restarts.
  *
  *                         Checking the table first avoids repreparing unnecessarily, but the cost of the query is not
  *                         always worth the improvement, especially if the number of statements is low.
  *
  *                         If the table does not exist, or the query fails for any other reason, the error is ignored
  *                         and the driver proceeds to reprepare statements according to the other parameters.
  * @param maxStatements    The maximum number of statements that should be reprepared. 0 or a negative value means no
  *                         limit.
  * @param maxParallelism   The maximum number of concurrent requests when repreparing.
  * @param timeout          The request timeout. This applies both to querying the system.prepared_statements table (if
  *                         relevant), and the prepare requests themselves.
  */
final case class ReprepareOnUp(enabled: Boolean = ReprepareOnUp.Default.enabled,
                               checkSystemTable: Boolean = ReprepareOnUp.Default.checkSystemTable,
                               maxStatements: Int = ReprepareOnUp.Default.maxStatements,
                               maxParallelism: Int = ReprepareOnUp.Default.maxParallelism,
                               timeout: Duration = ReprepareOnUp.Default.timeout)

object ReprepareOnUp {
  val Default: ReprepareOnUp = ReprepareOnUp(true, false, 0, 100, InitQueryTimeout)
}

/** Options related to the Netty event loop groups used internally by the driver.
  *
  * @param daemon     Whether the threads created by the driver should be daemon threads.
  *                   This will apply to the threads in io-group, admin-group, and the timer thread.
  * @param ioGroup    The event loop group used for I/O operations (reading and writing to Cassandra nodes).
  *                   By default, threads in this group are named after the session name, "-io-" and an incrementing
  *                   counter, for example "s0-io-0".
  * @param adminGroup The event loop group used for admin tasks not related to request I/O (handle cluster events,
  *                   refresh metadata, schedule reconnections, etc.)
  *                   By default, threads in this group are named after the session name, "-admin-" and an
  *                   incrementing counter, for example "s0-admin-0".
  * @param timer      The timer used for scheduling request timeouts and speculative executions
  *                   By default, this thread is named after the session name and "-timer-0", for example
  *                   "s0-timer-0".
  */
final case class Netty(daemon: Boolean = Netty.Default.daemon,
                       ioGroup: Group = Netty.Default.ioGroup,
                       adminGroup: Group = Netty.Default.adminGroup,
                       timer: Timer = Netty.Default.timer)

object Netty {
  val Default: Netty = Netty(false, Group.Default, Group.Default, Timer.Default)
}

/** The event loop group used for I/O operations (reading and writing to Cassandra nodes).
  *
  * @param size     The number of threads.
  *                 If this is set to 0, the driver will use `Runtime.getRuntime().availableProcessors() * 2`.
  * @param shutdown The options to shut down the event loop group gracefully when the driver closes. If a task
  *                 gets submitted during the quiet period, it is accepted and the quiet period starts over.
  *                 The timeout limits the overall shutdown time.
  */
final case class Group(size: Int, shutdown: Shutdown)

object Group {
  val Default: Group = Group(2, Shutdown.Default)
}

/** The options to shut down the event loop group gracefully when the driver closes. If a task
  * gets submitted during the quiet period, it is accepted and the quiet period starts over.
  *
  * The timeout limits the overall shutdown time.
  */
final case class Shutdown(quietPeriod: Int, timeout: Int, unit: String)

object Shutdown {
  val Default: Shutdown = Shutdown(2, 15, "SECONDS")
}

/** The timer used for scheduling request timeouts and speculative executions.
  *
  * @param tickDuration  The timer tick duration.
  *                      This is how frequent the timer should wake up to check for timed-out tasks or speculative
  *                      executions. Lower resolution (i.e. longer durations) will leave more CPU cycles for running
  *                      I/O operations at the cost of precision of exactly when a request timeout will expire or a
  *                      speculative execution will run. Higher resolution (i.e. shorter durations) will result in
  *                      more precise request timeouts and speculative execution scheduling, but at the cost of CPU
  *                      cycles taken from I/O operations, which could lead to lower overall I/O throughput.
  *
  *                      The default value is 100 milliseconds, which is a comfortable value for most use cases.
  *                      However if you are using more agressive timeouts or speculative execution delays, then you
  *                      should lower the timer tick duration as well, so that its value is always equal to or lesser
  *                      than the timeout duration and/or speculative execution delay you intend to use.
  *
  *                      Note for Windows users: avoid setting this to aggressive values, that is, anything under 100
  *                      milliseconds; doing so is known to cause extreme CPU usage. Also, the tick duration must be
  *                      a multiple of 10 under Windows; if that is not the case, it will be automatically rounded
  *                      down to the nearest multiple of 10 (e.g. 99 milliseconds will be rounded down to 90
  *                      milliseconds).
  * @param ticksPerWheel Number of ticks in a Timer wheel. The underlying implementation uses Netty's
  *                      HashedWheelTimer, which uses hashes to arrange the timeouts. This effectively controls the
  *                      size of the timer wheel.
  */
final case class Timer(tickDuration: Duration = Timer.Default.tickDuration, ticksPerWheel: Int = Timer.Default.ticksPerWheel)

object Timer {
  val Default: Timer = Timer(100.milliseconds, 2048)
}

/** The component that coalesces writes on the connections.
  * This is exposed mainly to facilitate tuning during development. You shouldn't have to adjust this.
  *
  * @param maxRunsWithNoWork  How many times the coalescer is allowed to reschedule itself when it did no work.
  * @param rescheduleInterval The reschedule interval.
  */
final case class Coalescer(maxRunsWithNoWork: Int = Coalescer.Default.maxRunsWithNoWork,
                           rescheduleInterval: Duration = Coalescer.Default.rescheduleInterval)

object Coalescer {
  val Default: Coalescer = Coalescer(5, 10.microseconds)
}
