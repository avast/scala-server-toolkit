package com.avast.server.toolkit.execution

import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
import java.util.concurrent.{
  BlockingQueue,
  ExecutorService,
  ForkJoinPool,
  LinkedBlockingQueue,
  SynchronousQueue,
  ThreadFactory,
  ThreadPoolExecutor,
  TimeUnit
}

import cats.effect.{Blocker, Resource, Sync}
import com.avast.server.toolkit.execution.ConfigurableThreadFactory.Config

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}
import scala.language.higherKinds

/** Provides necessary executors - the default one for execution of your business logic and callbacks and special one designated for
  * blocking operations. Also allows you to create more executors if you need them.
  */
class ExecutorModule[F[_]: Sync](val numOfCpus: Int, val executor: ExecutionContext, blockingExecutor: ExecutionContextExecutorService) {
  module =>

  /** Executor designated for blocking operations. */
  val blocker: Blocker = Blocker.liftExecutionContext(blockingExecutor)

  /** [[java.util.concurrent.ExecutorService]] that can be used for blocking operations in Java-interop code. */
  lazy val blockingExecutorService: ExecutorService = blockingExecutor

  /** Provides implicit reference to the default callback executor for easier use. */
  object Implicits {

    implicit val executor: ExecutionContext = module.executor

  }

  /** Makes [[java.util.concurrent.ThreadPoolExecutor]] according to the given config and with [[java.util.concurrent.ThreadFactory]]. */
  def makeThreadPoolExecutor(config: ThreadPoolExecutorConfig,
                             threadFactory: ThreadFactory): Resource[F, ExecutionContextExecutorService] = {
    ExecutorModule.makeThreadPoolExecutor(config, threadFactory, new LinkedBlockingQueue).map(ExecutionContext.fromExecutorService)
  }

  /** Makes [[java.util.concurrent.ForkJoinPool]] according to the given config and with [[java.util.concurrent.ThreadFactory]]. */
  def makeForkJoinExecutor(config: ForkJoinPoolConfig,
                           threadFactory: ForkJoinWorkerThreadFactory): Resource[F, ExecutionContextExecutorService] = {
    ExecutorModule.makeForkJoinPool(config, numOfCpus, threadFactory).map(ExecutionContext.fromExecutorService)
  }

}

object ExecutorModule {

  /** Makes [[ExecutorModule]] with default callback executor and extra [[cats.effect.Blocker]] executor for blocking operations. */
  def make[F[_]: Sync]: Resource[F, ExecutorModule[F]] = {
    for {
      numOfCpus <- Resource.liftF(Sync[F].delay(Runtime.getRuntime.availableProcessors))
      coreSize = numOfCpus * 2
      executor <- makeThreadPoolExecutor(ThreadPoolExecutorConfig(coreSize, coreSize), toolkitThreadFactory, new LinkedBlockingQueue)
                   .map(ExecutionContext.fromExecutorService)
      be <- makeBlockingExecutor.map(ExecutionContext.fromExecutorService)
    } yield new ExecutorModule[F](numOfCpus, executor, be)
  }

  /** Makes [[ExecutorModule]] with the provided callback executor and extra [[cats.effect.Blocker]] executor for blocking operations. */
  def make[F[_]: Sync](executor: ExecutionContext): Resource[F, ExecutorModule[F]] = {
    for {
      numOfCpus <- Resource.liftF(Sync[F].delay(Runtime.getRuntime.availableProcessors))
      be <- makeBlockingExecutor.map(ExecutionContext.fromExecutorService)
    } yield new ExecutorModule[F](numOfCpus, executor, be)
  }

  /** Makes [[ExecutorModule]] with executor and extra [[cats.effect.Blocker]] executor for blocking operations. */
  def make[F[_]: Sync](executorConfig: ThreadPoolExecutorConfig): Resource[F, ExecutorModule[F]] = {
    for {
      numOfCpus <- Resource.liftF(Sync[F].delay(Runtime.getRuntime.availableProcessors))
      executor <- makeThreadPoolExecutor(executorConfig, toolkitThreadFactory, new LinkedBlockingQueue)
                   .map(ExecutionContext.fromExecutorService)
      be <- makeBlockingExecutor.map(ExecutionContext.fromExecutorService)
    } yield new ExecutorModule[F](numOfCpus, executor, be)
  }

  /** Makes [[ExecutorModule]] with fork-join executor and extra [[cats.effect.Blocker]] executor for blocking operations. */
  def makeWithForkJoin[F[_]: Sync](executorConfig: ForkJoinPoolConfig): Resource[F, ExecutorModule[F]] = {
    for {
      numOfCpus <- Resource.liftF(Sync[F].delay(Runtime.getRuntime.availableProcessors))
      executor <- makeForkJoinPool(executorConfig, numOfCpus, toolkitThreadFactory)
                   .map(ExecutionContext.fromExecutorService)
      blockingExecutor <- makeBlockingExecutor.map(ExecutionContext.fromExecutorService)
    } yield new ExecutorModule[F](numOfCpus, executor, blockingExecutor)
  }

  private def makeBlockingExecutor[F[_]: Sync] =
    makeThreadPoolExecutor[F](
      ThreadPoolExecutorConfig(0, Int.MaxValue),
      new ConfigurableThreadFactory(Config(nameFormat = Some("default-blocking-%02d"), daemon = true)),
      new SynchronousQueue
    )

  private def toolkitThreadFactory = new ConfigurableThreadFactory(Config(nameFormat = Some("default-async-%02d"), daemon = true))

  private def makeThreadPoolExecutor[F[_]: Sync](config: ThreadPoolExecutorConfig,
                                                 threadFactory: ThreadFactory,
                                                 queue: BlockingQueue[Runnable]): Resource[F, ThreadPoolExecutor] = {
    Resource.make {
      Sync[F].delay {
        val threadPool = new ThreadPoolExecutor(
          config.coreSize,
          config.maxSize,
          config.keepAlive.toMillis,
          TimeUnit.MILLISECONDS,
          queue,
          threadFactory
        )
        threadPool.allowCoreThreadTimeOut(true)

        threadPool
      }
    }(pool => Sync[F].delay(pool.shutdown()))
  }

  private def makeForkJoinPool[F[_]: Sync](config: ForkJoinPoolConfig,
                                           numOfCpus: Int,
                                           threadFactory: ForkJoinWorkerThreadFactory): Resource[F, ForkJoinPool] = {
    Resource.make {
      Sync[F].delay {
        new ForkJoinPool(config.computeParallelism(numOfCpus), threadFactory, LoggingUncaughtExceptionHandler, config.computeAsyncMode)
      }
    }(pool => Sync[F].delay(pool.shutdown()))
  }

}
