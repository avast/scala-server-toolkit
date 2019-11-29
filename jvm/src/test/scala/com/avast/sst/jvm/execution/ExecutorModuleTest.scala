package com.avast.sst.jvm.execution

import cats.effect.SyncIO
import org.scalatest.funsuite.AnyFunSuite

class ExecutorModuleTest extends AnyFunSuite {

  test("ExecutorModule initializes properly and blocking executor differs from callback executor") {
    val executorModule = ExecutorModule.makeDefault[SyncIO].use(m => SyncIO.pure(m)).unsafeRunSync()
    assert(executorModule.executionContext !== executorModule.blocker.blockingContext)
  }

}
