package com.avast.sst.datastax.config

/** Configuration for datastax java driver.
  */
final case class DatastaxDriverConfig(basic: Basic, advanced: Advanced = Advanced.Default, profiles: List[Profile] = List.empty)
