package com.avast.sst.akka.http.server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.{ConnectionContext, Http}
import cats.effect.{Resource, Sync}
import com.avast.sst.akka.http.server.config.{AkkaHttpServerConfig, AkkaHttpServerConnectionContextConfig, AkkaHttpServerSslConfig}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.language.higherKinds

object AkkaHttpServerModule {

  private val logger = LoggerFactory.getLogger(getClass)

  /**
    * Makes [[Http.ServerBinding]] initialized with the given config.
    */
  def makeOrRaise[F[_]: Sync](config: AkkaHttpServerConfig,
                              sslConfig: Option[AkkaHttpServerSslConfig],
                              routes: Route,
                              executionContext: ExecutionContext,
                              actorSystem: ActorSystem): Resource[F, Http.ServerBinding] = {
    Resource.make {
      import cats.Monad.ops._
      for {
        _ <- Sync[F].delay(logger.info(s"Starting Akka HTTP server..."))
        connectionContext <- createConnectionContext(config, sslConfig)
        binding <- Sync[F].delay {
          implicit val ec: ExecutionContext = executionContext
          implicit val as: ActorSystem = actorSystem
          val _ = akka.http.scaladsl.settings.ServerSettings.default
          val bindingFuture = Http().bindAndHandle(routes, config.listenHostname, config.listenPort, connectionContext)
          val binding = Await.result(bindingFuture, config.startupTimeout)
          logger.info(
            s"Akka HTTP server successfully started, listening on ${binding.localAddress.getHostName}:${binding.localAddress.getPort}")
          binding
        }
      } yield binding
    } { binding =>
      Sync[F].delay {
        logger.info("Shutting down Akka HTTP server...")
        val unbindFuture = binding.terminate(config.shutdownTimeout)
        val _ = Await.ready(unbindFuture, config.shutdownTimeout + 1.second)
      }
    }
  }

  private def createConnectionContext[F[_]: Sync](config: AkkaHttpServerConfig, sslConfig: Option[AkkaHttpServerSslConfig]): F[ConnectionContext] = {
    (config.connectionContext, sslConfig) match {
      case (AkkaHttpServerConnectionContextConfig.NoEncryption, _) =>
        Sync[F].pure(ConnectionContext.noEncryption())
      case (AkkaHttpServerConnectionContextConfig.Https(cipherSuites, protocols, clientAuth, _, _), Some(ssl)) =>
        Sync[F].pure(ConnectionContext.https(ssl.sslContext,
                                None,
                                cipherSuites.map(_.to[collection.immutable.Seq]),
                                protocols.map(_.to[collection.immutable.Seq]),
                                clientAuth,
                                ssl.sslParameters))
      case (_: AkkaHttpServerConnectionContextConfig.Https, None) =>
        Sync[F].raiseError(new IllegalArgumentException("Missing sslConfig for Https connection context."))
    }
  }

}
