package com.avast.sst.micrometer.prometheus

import cats.effect.*
import monix.eval.Task
import monix.eval.instances.CatsConcurrentEffectForTask
import monix.execution.Scheduler
import org.http4s.metrics.prometheus.PrometheusExportService
import org.scalatest.funsuite.AnyFunSuite

import java.util.concurrent.{SynchronousQueue, ThreadPoolExecutor, TimeUnit}
import scala.concurrent.ExecutionContext

class Http4sPrometheusCompatibilityTest extends AnyFunSuite {

  implicit def scheduler: Scheduler = Scheduler.global

  protected def options: Task.Options = Task.defaultOptions.withSchedulerFeatures(scheduler)

  protected implicit lazy val catsEffect: ConcurrentEffect[Task] =
    new CatsConcurrentEffectForTask()(scheduler, options)

  test("Http4s Prometheus compatibility test") {

    val config = MicrometerPrometheusConfig()

    val blockingExecutor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new SynchronousQueue())
    val blocker = Blocker.liftExecutionContext(ExecutionContext.fromExecutor(blockingExecutor))

    val test = for {

      prometheusMeterRegistry <- MicrometerPrometheusModule.make(config, blocker)
      _ = PrometheusExportService(prometheusMeterRegistry.getPrometheusRegistry)
      _ <- PrometheusExportService.addDefaults(prometheusMeterRegistry.getPrometheusRegistry)
    } yield ()

    test.use(_ => Task.unit).runSyncUnsafe()

  }
}
