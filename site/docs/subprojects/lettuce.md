---
layout: docs
title: "Lettuce (Redis)"
---

# FS2 Kafka

`libraryDependencies += "com.avast" %% "sst-lettuce" % "@VERSION@"`

This subproject initializes [Lettuce](https://lettuce.io) Redis driver:

```scala mdoc:silent
import cats.effect.Resource
import com.avast.sst.lettuce.{LettuceConfig, LettuceModule}
import io.lettuce.core.codec.{RedisCodec, StringCodec}
import zio._
import zio.interop.catz._

implicit val runtime = zio.Runtime.default // this is just needed in example

implicit val lettuceCodec: RedisCodec[String, String] = StringCodec.UTF8

for {
  connection <- LettuceModule.makeConnection[Task, String, String](LettuceConfig("redis://localhost"))
  value <- Resource.eval(Task.effect(connection.sync().get("key")))
} yield value
```
