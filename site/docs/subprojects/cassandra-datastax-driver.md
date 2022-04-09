---
layout: docs
title: "Datastax Cassandra Driver"
---

# Datastax Cassandra Driver

This subproject initializes Datastax Cassandra driver's `Session`.

`libraryDependencies += "com.avast" %% "sst-cassandra-datastax-driver" % "@VERSION@"`

```scala mdoc:silent
import cats.effect.Resource
import com.avast.sst.datastax.CassandraDatastaxDriverModule
import com.avast.sst.datastax.config.CassandraDatastaxDriverConfig
import com.avast.sst.datastax.pureconfig.implicits.*
import com.avast.sst.pureconfig.PureConfigModule
import zio.*
import zio.interop.catz.*

implicit val runtime: Runtime[ZEnv] = zio.Runtime.default // this is just needed in example

for {
    configuration <- Resource.eval(PureConfigModule.makeOrRaise[Task, CassandraDatastaxDriverConfig])
    db <- CassandraDatastaxDriverModule.make[Task](configuration)
} yield db
```

```HOCON
basic {
  contact-points = ["localhost:9042"]

  load-balancing-policy {
    local-datacenter = "datacenter1"
  }
}
```
