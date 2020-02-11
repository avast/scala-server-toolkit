package com.avast.sst.datastax.config

/** Configuration for Cassandra Datastax Driver. */
final case class CassandraDatastaxDriverConfig(
    basic: BasicConfig,
    advanced: AdvancedConfig = AdvancedConfig.Default,
    profiles: List[ProfileConfig] = List.empty
)
