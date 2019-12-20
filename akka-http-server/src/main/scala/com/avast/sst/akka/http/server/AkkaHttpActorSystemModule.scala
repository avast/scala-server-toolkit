package com.avast.sst.akka.http.server

import akka.actor.ActorSystem
import cats.effect.{Resource, Sync}
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.higherKinds
import scala.util.Try

// This is only temporarily part of this module
//   It should be moved into separate module and its configuration reworked into case class
object AkkaHttpActorSystemModule {

  def make[F[_]: Sync](tsConfig: Config): Resource[F, ActorSystem] = {
    Resource.make {
      Sync[F].delay {
        ActorSystem.create("akka-http-system", tsConfig)
      }
    } { as =>
      Sync[F].delay {
        val akkaDefaults = ConfigFactory.load(ActorSystem.getClass.getClassLoader).getConfig("akka") // load reference.conf from JAR com.typesafe.akka:akka-actor_?
        val configWithDefaults = tsConfig.withFallback(akkaDefaults)
        val defaultTimeout = configWithDefaults.getDuration("coordinated-shutdown.default-phase-timeout")
        val phasesConfig = configWithDefaults.getConfig("coordinated-shutdown.phases")
        val finalPhase = "actor-system-terminate"
        val totalDuration = accumulatePhaseTimeoutChain(defaultTimeout.toMillis, phasesConfig)(finalPhase).millis
        val terminateFuture = as.terminate() // is there a difference with `akka.actor.CoordinatedShutdown(as).runAll(ActorSystemTerminateReason)`?
        val _ = Await.result(terminateFuture, totalDuration + 1.second)
      }
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Recursion")) // This can not reasonably run out of stack space, only in case of cycle which would mean all sorts of problems for akka
  private def accumulatePhaseTimeoutChain(defaultTimeoutMillis: Long, phasesConfig: Config)(phase: String): Long = {
    val currentTimeout = Try(phasesConfig.getDuration(s"$phase.timeout")).fold(_ => defaultTimeoutMillis, d => d.toMillis)
    Try(phasesConfig.getStringList(s"$phase.depends-on")).toOption match {
      case None => currentTimeout
      case Some(parentPhases) =>
        currentTimeout + parentPhases.asScala.map(accumulatePhaseTimeoutChain(defaultTimeoutMillis, phasesConfig)).sum
    }
  }

}
