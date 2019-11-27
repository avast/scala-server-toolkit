# Datastax Cassandra Driver

This module initializes Datastax Cassandra driver's `Session`.

`libraryDependencies += "com.avast" %% "sst-cassandra-datastax-driver" % "<VERSION>"`

```scala mdoc:silent
import cats.effect.Resource
import com.avast.sst.datastax.CassandraDatastaxDriverModule
import com.avast.sst.example.config.Configuration
import com.avast.sst.pureconfig.PureConfigModule
import zio._
import zio.interop.catz._

implicit val runtime = new DefaultRuntime {} // this is just needed in example

for {
    configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
    db <- CassandraDatastaxDriverModule.make[Task](configuration.datastaxDriver)
} yield db
```

```HOCON
datastax-driver {
  basic {
    contact-points = [
      "localhost:9042"
    ]
    load-balancing-policy {
      local-datacenter = "datacenter1"
    }
  }
}
```