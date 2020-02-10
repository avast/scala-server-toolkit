package com.avast.sst.grpc.server

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration

final case class GrpcServerConfig(port: Int,
                                  handshakeTimeout: Duration = Duration(120, TimeUnit.SECONDS),
                                  maxInboundMessageSize: Int = 4 * 1024 * 1024,
                                  maxInboundMetadataSize: Int = 8192,
                                  serverShutdownTimeout: Duration = Duration(10, TimeUnit.SECONDS))
