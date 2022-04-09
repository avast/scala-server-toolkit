package com.avast.sst.fs2kafka

import cats.effect.{IO, Resource}
import cats.syntax.flatMap._
import com.dimafeng.testcontainers.{ForAllTestContainer, KafkaContainer}
import fs2.kafka.{AutoOffsetReset, ProducerRecord, ProducerRecords}
import org.scalatest.funsuite.AsyncFunSuite

import scala.concurrent.ExecutionContext.Implicits.global

class Fs2KafkaModuleTest extends AsyncFunSuite with ForAllTestContainer {

  override val container = KafkaContainer()

  implicit private val cs = IO.contextShift(global)
  implicit private val timer = IO.timer(global)

  test("producer") {
    val io = for {
      producer <- Fs2KafkaModule.makeProducer[IO, String, String](ProducerConfig(List(container.bootstrapServers)))
      consumer <- Fs2KafkaModule.makeConsumer[IO, String, String](
        ConsumerConfig(List(container.bootstrapServers), groupId = "test", autoOffsetReset = AutoOffsetReset.Earliest)
      )
      _ <- Resource.eval(consumer.subscribeTo("test"))
      _ <- Resource.eval(producer.produce(ProducerRecords.one(ProducerRecord("test", "key", "value"))).flatten)
      event <- Resource.eval(consumer.stream.head.compile.toList)
    } yield assert(event.head.record.key === "key" && event.head.record.value === "value")

    io.use(IO.pure).unsafeToFuture()
  }

}
