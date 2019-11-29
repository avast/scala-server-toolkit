package com.avast.sst.jvm.system.random

import cats.effect.SyncIO
import org.scalatest.funsuite.AnyFunSuite

class RandomModuleTest extends AnyFunSuite {

  test("RandomModule initializes properly") {
    val test = for {
      random <- RandomModule.makeRandom[SyncIO](123L)
      number <- random.nextInt
    } yield assert(number === -1188957731L)

    test.unsafeRunSync()
  }

}
