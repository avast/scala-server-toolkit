package com.avast.sst.fs2kafka

import org.scalatest.funsuite.AnyFunSuite

import scala.annotation.nowarn

@nowarn("msg=unused value")
class KafkaConfigTest extends AnyFunSuite {

  test("verify ConsumerConfig defaults") {
    ConsumerConfig(List.empty, "group.id")
    succeed
  }

  test("verify ProducerConfig defaults") {
    ProducerConfig(List.empty)
    succeed
  }

}
