---
layout: docs
title: "FS2 Kafka"
---

# FS2 Kafka

`libraryDependencies += "com.avast" %% "sst-fs2-kafka" % "@VERSION@"`

This subproject initializes [FS2 Kafka](https://github.com/fd4s/fs2-kafka) consumer or producer:

```scala mdoc:silent
import cats.effect.Resource
import cats.syntax.flatMap._
import com.avast.sst.fs2kafka._
import fs2.kafka.{AutoOffsetReset, ProducerRecord, ProducerRecords}
import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

implicit val runtime = zio.Runtime.default // this is just needed in example

for {
  consumer <- Fs2KafkaModule.makeConsumer[Task, String, String](
    ConsumerConfig(List("localhost:9092"), groupId = "test", autoOffsetReset = AutoOffsetReset.Earliest)
  )
  _ <- Resource.liftF(consumer.subscribeTo("test"))
  consumerStream <- Resource.liftF(consumer.stream)
} yield consumerStream
```

The configuration of Kafka client is very large therefore you can either use the provided configuration case class, or you can use the underlying
`ConsumerSettings`/`ProducerSettings` builders directly. 
