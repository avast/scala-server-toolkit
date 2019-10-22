# Flyway

[![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-flyway_2.12)](https://repo1.maven.org/maven2/com/avast/sst-flyway_2.12/)

`libraryDependencies += "com.avast" %% "sst-flyway" % "<VERSION>"`

This module initializes `Flyway` which can be used to do automated SQL DB migrations. See the [documentation of Flyway](https://flywaydb.org/documentation/) 
on how to go about that.

The method `make` requires `javax.sql.DataSource` which you can for example obtain from `doobie-hikari` module:

```scala
import cats.effect.Resource
import com.avast.sst.doobie.DoobieHikariModule
import com.avast.sst.flyway.FlywayModule
import zio.Task
import zio.interop.catz._
import zio.interop.catz.implicits._

for {
  doobieTransactor <- DoobieHikariModule.make[Task](???, ???, ???, ???)
  flyway <- Resource.liftF(FlywayModule.make[Task](doobieTransactor.kernel, ???))
} yield ()
```
