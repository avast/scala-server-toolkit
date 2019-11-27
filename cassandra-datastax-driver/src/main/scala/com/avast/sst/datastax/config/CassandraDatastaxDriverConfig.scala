package com.avast.sst.datastax.config

/** Configuration for Cassandra Datastax Driver. */
final case class CassandraDatastaxDriverConfig(basic: Basic, advanced: Advanced = Advanced.Default, profiles: List[Profile] = List.empty)
