package com.avast.sst.datastax.config


/** Configuration for datastax java driver.
  */
final case class DatastaxDriverConfig(basic: Basic, advanced: Advanced, profiles: List[Profile] = List.empty)
