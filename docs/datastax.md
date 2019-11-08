# Datastax

This module initializes Datastax driver's `Session`.

```scala
import cats.effect.Resource
import com.avast.sst.datastax.DatastaxModule
import com.avast.sst.example.config.Configuration
import com.avast.sst.pureconfig.PureConfigModule
import zio._
import zio.interop.catz._

implicit val runtime = new DefaultRuntime {} // this is just needed in example

for {
configuration <- Resource.liftF(PureConfigModule.makeOrRaise[Task, Configuration])
db <- DatastaxModule.make[Task](configuration.datastaxDriver)
} yield db
```

```
datastax-driver {
  basic {
    session-keyspace = "some_keyspace"
    contact-points = [
      "localhost:9045"
    ]
    load-balancing-policy {
      class = "DefaultLoadBalancingPolicy"
      local-datacenter = "datacenter1"
    }
  }
}
```