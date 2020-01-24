# Datastax Cassandra Driver

[![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-cassandra-datastax-driver_2.12)](https://repo1.maven.org/maven2/com/avast/sst-cassandra-datastax-driver_2.12/)

This subproject initializes Datastax Cassandra driver's `Session`.

`libraryDependencies += "com.avast" %% "sst-cassandra-datastax-driver" % "<VERSION>"`

```scala
import cats.effect.Resource
import com.avast.sst.datastax.CassandraDatastaxDriverModule
import com.avast.sst.datastax.config.CassandraDatastaxDriverConfig
import com.avast.sst.datastax.pureconfig.implicits._
import com.avast.sst.pureconfig.PureConfigModule
import zio._
import zio.interop.catz._

implicit val runtime = new DefaultRuntime {} // this is just needed in example

for {
    configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, CassandraDatastaxDriverConfig])
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