package com.avast.sst.grpc.server

import java.util.concurrent.TimeUnit

import cats.effect.{Resource, Sync}
import io.grpc.{Server, ServerBuilder, ServerInterceptor, ServerServiceDefinition}

import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContext
object GrpcServerModule {

  /** Makes [[io.grpc.Server]] (Netty) initialized with the given config, services and interceptors.
    *
    * @param services service implementations to be added to the handler registry
    * @param executionContext executor to be used for the server
    * @param interceptors that are run for all the services
    */
  def make[F[_]: Sync](config: GrpcServerConfig,
                       services: Seq[ServerServiceDefinition],
                       executionContext: ExecutionContext,
                       interceptors: Seq[ServerInterceptor] = List.empty): Resource[F, Server] =
    Resource.make {
      Sync[F].delay {
        val builder = ServerBuilder
          .forPort(config.port)
          .handshakeTimeout(config.handshakeTimeout.toMillis, TimeUnit.MILLISECONDS)
          .maxInboundMessageSize(config.maxInboundMessageSize)
          .maxInboundMetadataSize(config.maxInboundMetadataSize)
          .executor(executionContext.execute)

        services.foreach(builder.addService)
        interceptors.foreach(builder.intercept)

        builder.build.start()
      }
    } { s =>
      Sync[F].delay {
        s.shutdown().awaitTermination(config.serverShutdownTimeout.toMillis, TimeUnit.MILLISECONDS)
        ()
      }
    }

}
