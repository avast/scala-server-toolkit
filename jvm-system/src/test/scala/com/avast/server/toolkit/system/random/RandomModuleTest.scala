package com.avast.server.toolkit.system.random

import cats.effect.SyncIO
import org.scalatest.FunSuite

class RandomModuleTest extends FunSuite {

  test("RandomModule initializes properly") {
    val test = for {
      random <- RandomModule.makeRandom[SyncIO](123L)
      number <- random.nextInt
    } yield assert(number === -1188957731L)

    test.unsafeRunSync()
  }

}
