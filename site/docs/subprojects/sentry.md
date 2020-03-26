---
layout: docs
title: "Sentry"
---

# Sentry

`libraryDependencies += "com.avast" %% "sst-sentry" % "@VERSION@"`

This subproject allows you to initialize `SentryClient` from a configuration case class. There are two `make*` methods. The one called `make`
does everything according to the configuration. The one called `makeWithReleaseFromPackage` adds a bit of clever behavior because it reads
the `Implementation-Version` property from the `MANIFEST.MF` file from the JAR (package) of the respective `Main` class and uses it to override
the `release` property of Sentry. This allows you to automatically propage the version of your application to Sentry.

Initialization of the `SentryClient` is side-effectful so it is wrapped in `Resource[F, SentryClient]` and `F` is `Sync`.

```scala mdoc:silent
import com.avast.sst.sentry._
import zio.interop.catz._
import zio.Task

val sentry = SentryModule.make[Task](SentryConfig("<dsn>"))
```
