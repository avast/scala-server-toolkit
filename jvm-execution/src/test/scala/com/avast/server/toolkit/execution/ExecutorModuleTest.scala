package com.avast.server.toolkit.execution

import cats.effect.SyncIO
import org.scalatest.FunSuite

class ExecutorModuleTest extends FunSuite {

  test("ExecutorModule") {
    val executorModule = ExecutorModule.make[SyncIO].use(m => SyncIO.pure(m)).unsafeRunSync()
    assert(executorModule.executor !== executorModule.blocker.blockingContext)
  }

}
