package com.avast.server.toolkit.execution

import cats.effect.SyncIO
import org.scalatest.FunSuite

class ExecutorModuleTest extends FunSuite {

  test("ExecutorModule initializes properly and blocking executor differs from callback executor") {
    val executorModule = ExecutorModule.makeDefault[SyncIO].use(m => SyncIO.pure(m)).unsafeRunSync()
    assert(executorModule.executionContext !== executorModule.blocker.blockingContext)
  }

}
