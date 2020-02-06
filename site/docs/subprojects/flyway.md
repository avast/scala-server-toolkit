---
layout: docs
title: "Flyway"
---

# Flyway

`libraryDependencies += "com.avast" %% "sst-flyway" % "@VERSION@"`

This subproject initializes `Flyway` which can be used to do automated SQL DB migrations. See the [documentation of Flyway](https://flywaydb.org/documentation/) 
on how to go about that.

The method `make` requires `javax.sql.DataSource` which you can for example obtain from `doobie-hikari` subproject:

```scala mdoc:compile-only
import cats.effect.Resource
import com.avast.sst.doobie.DoobieHikariModule
import com.avast.sst.flyway.FlywayModule
import zio.Task
import zio.interop.catz._

for {
  doobieTransactor <- DoobieHikariModule.make[Task](???, ???, ???, ???)
  flyway <- Resource.liftF(FlywayModule.make[Task](doobieTransactor.kernel, ???))
  _ <- Resource.liftF(Task.effect(flyway.migrate()))
} yield ()
```
