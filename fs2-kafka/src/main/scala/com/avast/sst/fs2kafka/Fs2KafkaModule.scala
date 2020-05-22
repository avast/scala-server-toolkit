package com.avast.sst.fs2kafka

import cats.effect.{Blocker, ConcurrentEffect, ContextShift, Resource, Timer}
import fs2.kafka._

import scala.util.chaining.scalaUtilChainingOps

object Fs2KafkaModule {

  def makeConsumer[F[_]: ConcurrentEffect: ContextShift: Timer, K: Deserializer[F, *], V: Deserializer[F, *]](
      config: ConsumerConfig,
      blocker: Option[Blocker] = None
  ): Resource[F, KafkaConsumer[F, K, V]] = {
    def setOpt[A](maybeValue: Option[A])(
        setter: (ConsumerSettings[F, K, V], A) => ConsumerSettings[F, K, V]
    )(initial: ConsumerSettings[F, K, V]): ConsumerSettings[F, K, V] =
      maybeValue match {
        case Some(value) => setter(initial, value)
        case None        => initial
      }

    val settings = ConsumerSettings(implicitly[Deserializer[F, K]], implicitly[Deserializer[F, V]])
      .withBootstrapServers(config.bootstrapServers.mkString(","))
      .withGroupId(config.groupId)
      .pipe(setOpt(config.groupInstanceId)(_.withGroupInstanceId(_)))
      .pipe(setOpt(config.clientId)(_.withClientId(_)))
      .pipe(setOpt(config.clientRack)(_.withClientRack(_)))
      .withAutoOffsetReset(config.autoOffsetReset)
      .withEnableAutoCommit(config.enableAutoCommit)
      .withAutoCommitInterval(config.autoCommitInterval)
      .withAllowAutoCreateTopics(config.allowAutoCreateTopics)
      .withCloseTimeout(config.closeTimeout)
      .withCommitRecovery(config.commitRecovery)
      .withCommitTimeout(config.closeTimeout)
      .withDefaultApiTimeout(config.defaultApiTimeout)
      .withHeartbeatInterval(config.heartbeatInterval)
      .withIsolationLevel(config.isolationLevel)
      .withMaxPrefetchBatches(config.maxPrefetchBatches)
      .withPollInterval(config.pollInterval)
      .withPollTimeout(config.pollTimeout)
      .withMaxPollInterval(config.maxPollInterval)
      .withMaxPollRecords(config.maxPollRecords)
      .withRequestTimeout(config.requestTimeout)
      .withSessionTimeout(config.sessionTimeout)
      .pipe(setOpt(blocker)(_.withBlocker(_)))

    makeConsumer(settings)
  }

  def makeConsumer[F[_]: ConcurrentEffect: ContextShift: Timer, K, V](
      settings: ConsumerSettings[F, K, V]
  ): Resource[F, KafkaConsumer[F, K, V]] = consumerResource[F].using(settings)

  def makeProducer[F[_]: ConcurrentEffect: ContextShift, K: Serializer[F, *], V: Serializer[F, *]](
      config: ProducerConfig,
      blocker: Option[Blocker] = None
  ): Resource[F, KafkaProducer[F, K, V]] = {
    def setOpt[A](maybeValue: Option[A])(
        setter: (ProducerSettings[F, K, V], A) => ProducerSettings[F, K, V]
    )(initial: ProducerSettings[F, K, V]): ProducerSettings[F, K, V] =
      maybeValue match {
        case Some(value) => setter(initial, value)
        case None        => initial
      }

    val settings = ProducerSettings(implicitly[Serializer[F, K]], implicitly[Serializer[F, V]])
      .withBootstrapServers(config.bootstrapServers.mkString(","))
      .pipe(setOpt(config.clientId)(_.withClientId(_)))
      .withAcks(config.acks)
      .withBatchSize(config.batchSize)
      .withCloseTimeout(config.closeTimeout)
      .withDeliveryTimeout(config.deliveryTimeout)
      .withRequestTimeout(config.requestTimeout)
      .withLinger(config.linger)
      .withEnableIdempotence(config.enableIdempotence)
      .withMaxInFlightRequestsPerConnection(config.maxInFlightRequestsPerConnection)
      .withParallelism(config.parallelism)
      .withRetries(config.retries)
      .pipe(setOpt(blocker)(_.withBlocker(_)))

    makeProducer(settings)
  }

  def makeProducer[F[_]: ConcurrentEffect: ContextShift, K, V](settings: ProducerSettings[F, K, V]): Resource[F, KafkaProducer[F, K, V]] =
    producerResource[F].using(settings)

}
