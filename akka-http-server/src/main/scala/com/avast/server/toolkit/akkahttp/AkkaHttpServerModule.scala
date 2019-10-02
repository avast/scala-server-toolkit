package com.avast.server.toolkit.akkahttp

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.effect.{ConcurrentEffect, Resource, Sync}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.language.higherKinds
import org.slf4j.LoggerFactory

object AkkaHttpServerModule {

  private val logger = LoggerFactory.getLogger("AkkaHttpServerModule")

  /**
    * Makes [[Http.ServerBinding]] initialized with the given config.
    */
  def make[F[_]: ConcurrentEffect](config: AkkaHttpServerConfig, routes: Route)
                                         (implicit executionContext: ExecutionContext, actorSystem: ActorSystem): Resource[F, Http.ServerBinding] = {
    Resource.make {
      implicitly[Sync[F]].delay {
        logger.info(s"Starting Akka HTTP server...")
        implicit val materializer: ActorMaterializer = ActorMaterializer()
        val bindingFuture = Http().bindAndHandle(routes, config.listenHostname, config.listenPort)
        val binding = Await.result(bindingFuture, config.startupTimeout)
        logger.info(s"Akka HTTP server successfully started, listening on ${binding.localAddress.getHostName}:${binding.localAddress.getPort}")
        binding
      }
    } { binding =>
      implicitly[Sync[F]].delay {
        logger.info("Shutting down Akka HTTP server...")
        val unbindFuture = binding.terminate(config.shutdownTimeout)
        Await.ready(unbindFuture, config.shutdownTimeout + 2.second)
        ()
      }
    }
  }

}
