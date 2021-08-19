package com.avast.sst.datastax.config

import scala.concurrent.duration._

/** Basic datastax driver configuration.
  *
  * @param contactPoints
  *   The contact points to use for the initial connection to the cluster. These are addresses of Cassandra nodes that the driver uses to
  *   discover the cluster topology.
  *
  * Only one contact point is required (the driver will retrieve the address of the other nodes automatically), but it is usually a good
  * idea to provide more than one contact point, because if that single contact point is unavailable, the driver cannot initialize itself
  * correctly. This must be a list of strings with each contact point specified as "host:port". If the host is a DNS name that resolves to
  * multiple A-records, all the corresponding addresses will be used. Do not use "localhost" as the host name (since it resolves to both
  * IPv4 and IPv6 addresses on some platforms).
  *
  * Note that Cassandra 3 and below requires all nodes in a cluster to share the same port (see CASSANDRA-7544).
  * @param sessionName
  *   A name that uniquely identifies the driver instance created from this configuration. This is used as a prefix for log messages and
  *   metrics.
  *
  * If this option is absent, the driver will generate an identifier composed of the letter 's' followed by an incrementing counter. If you
  * provide a different value, try to keep it short to keep the logs readable. Also, make sure it is unique: reusing the same value will not
  * break the driver, but it will mix up the logs and metrics.
  * @param sessionKeyspace
  *   The name of the keyspace that the session should initially be connected to.
  *
  * This expects the same format as in a CQL query: case-sensitive names must be quoted.
  *
  * If this option is absent, the session won't be connected to any keyspace, and you'll have to either qualify table names in your queries,
  * or use the per-query keyspace feature available in Cassandra 4 and above (see
  * `com.datastax.oss.driver.api.core.session.Request.getKeyspace()`).
  * @param request
  *   This configures basic request properties such as timeout, page size etc.
  * @param loadBalancingPolicy
  *   The policy that decides the "query plan" for each query; that is, which nodes to try as coordinators, and in which order.
  *
  * Overridable in a profile. Note that the driver creates as few instances as possible: if a named profile inherits from the default
  * profile, or if two sibling profiles have the exact same configuration, they will share a single policy instance at runtime. If there are
  * multiple load balancing policies in a single driver instance, they work together in the following way:
  *   - each request gets a query plan from its profile's policy (or the default policy if the request has no profile, or the profile does
  *     not override the policy).
  *   - when the policies assign distances to nodes, the driver uses the closest assigned distance for any given node.
  */
final case class BasicConfig(
    contactPoints: List[String] = BasicConfig.Default.contactPoints,
    sessionName: Option[String] = BasicConfig.Default.sessionName,
    sessionKeyspace: Option[String] = BasicConfig.Default.sessionKeyspace,
    configReloadInterval: Duration = BasicConfig.Default.configReloadInterval,
    request: BasicRequestConfig = BasicConfig.Default.request,
    loadBalancingPolicy: LoadBalancingPolicyConfig = BasicConfig.Default.loadBalancingPolicy
)

object BasicConfig {
  val Default: BasicConfig = BasicConfig(List.empty, None, None, 5.minutes, BasicRequestConfig.Default, LoadBalancingPolicyConfig.Default)
}

/** Request configuration.
  *
  * @param timeout
  *   How long the driver waits for a request to complete. This is a global limit on the duration of a `session.execute()` call, including
  *   any internal retries the driver might do.
  *
  * By default, this value is set pretty high to ensure that DDL queries don't time out, in order to provide the best experience for new
  * users trying the driver with the out-of-the-box configuration. For any serious deployment, we recommend that you use separate
  * configuration profiles for DDL and DML; you can then set the DML timeout much lower (down to a few milliseconds if needed).
  *
  * Note that, because timeouts are scheduled on the driver's timer thread, the duration specified here must be greater than the timer tick
  * duration defined by the advanced.netty.timer.tick-duration setting (see below). If that is not the case, timeouts will not be triggered
  * as timely as desired.
  *
  * Overridable in a profile.
  * @param consistency
  *   The consistency level. Overridable in a profile.
  * @param pageSize
  *   The page size. This controls how many rows will be retrieved simultaneously in a single network roundtrip (the goal being to avoid
  *   loading too many results in memory at the same time). If there are more results, additional requests will be used to retrieve them
  *   (either automatically if you iterate with the sync API, or explicitly with the async API's fetchNextPage method).
  *
  * If the value is 0 or negative, it will be ignored and the request will not be paged.
  *
  * Overridable in a profile.
  * @param serialConsistency
  *   The serial consistency level. The allowed values are SERIAL and LOCAL_SERIAL. Overridable in a profile.
  * @param defaultIdempotence
  *   The default idempotence of a request, that will be used for all `Request` instances where `isIdempotent()` returns null. Overridable
  *   in a profile.
  */
final case class BasicRequestConfig(
    timeout: Duration = BasicRequestConfig.Default.timeout,
    consistency: ConsistencyLevel = BasicRequestConfig.Default.consistency,
    pageSize: Int = BasicRequestConfig.Default.pageSize,
    serialConsistency: ConsistencyLevel = BasicRequestConfig.Default.serialConsistency,
    defaultIdempotence: Boolean = BasicRequestConfig.Default.defaultIdempotence
)

object BasicRequestConfig {
  val Default: BasicRequestConfig =
    BasicRequestConfig(RequestTimeout, ConsistencyLevel.LocalOne, RequestPageSize, ConsistencyLevel.Serial, false)
}

/** The policy that decides the "query plan" for each query; that is, which nodes to try as coordinators, and in which order.
  *
  * @param `class`
  *   The class of the policy. If it is not qualified, the driver assumes that it resides in the package
  *   `com.datastax.oss.driver.internal.core.loadbalancing`.
  * @param localDatacenter
  *   The datacenter that is considered "local": the default policy will only include nodes from this datacenter in its query plans.
  *
  * This option can only be absent if you specified no contact points: in that case, the driver defaults to 127.0.0.1:9042, and that node's
  * datacenter is used as the local datacenter.
  *
  * As soon as you provide contact points (either through the configuration or through the cluster builder), you must define the local
  * datacenter explicitly, and initialization will fail if this property is absent. In addition, all contact points should be from this
  * datacenter; warnings will be logged for nodes that are from a different one.
  *
  * @param filter
  *   A custom filter to include/exclude nodes.
  */
final case class LoadBalancingPolicyConfig(
    `class`: String = LoadBalancingPolicyConfig.Default.`class`,
    localDatacenter: Option[String] = LoadBalancingPolicyConfig.Default.localDatacenter,
    filter: Option[FilterConfig] = LoadBalancingPolicyConfig.Default.filter
)

object LoadBalancingPolicyConfig {
  val Default: LoadBalancingPolicyConfig = LoadBalancingPolicyConfig("DefaultLoadBalancingPolicy", None, None)
}

/** A custom filter to include/exclude nodes.
  *
  * The predicate's `test(Node)` method will be invoked each time the policy processes a topology or state change: if it returns false, the
  * node will be set at distance IGNORED (meaning the driver won't ever connect to it), and never included in any query plan.
  *
  * @param `class`
  *   it must be the fully-qualified name of a class that implements `java.util.function.Predicate<Node>`, and has a public constructor
  *   taking a single `DriverContext` argument.
  */
final case class FilterConfig(`class`: String)

/** The consistency level of a request
  */
sealed abstract class ConsistencyLevel {

  import ConsistencyLevel._

  def toStringRepr: String =
    this match {
      case Any         => "ANY"
      case One         => "ONE"
      case Two         => "TWO"
      case Three       => "THREE"
      case Quorum      => "QUORUM"
      case All         => "ALL"
      case LocalOne    => "LOCAL_ONE"
      case LocalQuorum => "LOCAL_QUORUM"
      case EachQuorum  => "EACH_QUORUM "
      case Serial      => "SERIAL"
      case LocalSerial => "LOCAL_SERIAL"
    }

}

object ConsistencyLevel {

  case object Any extends ConsistencyLevel
  case object One extends ConsistencyLevel
  case object Two extends ConsistencyLevel
  case object Three extends ConsistencyLevel
  case object Quorum extends ConsistencyLevel
  case object All extends ConsistencyLevel
  case object LocalOne extends ConsistencyLevel
  case object LocalQuorum extends ConsistencyLevel
  case object EachQuorum extends ConsistencyLevel
  case object Serial extends ConsistencyLevel
  case object LocalSerial extends ConsistencyLevel

}
