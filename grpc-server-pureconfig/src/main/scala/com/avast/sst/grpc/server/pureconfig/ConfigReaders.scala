package com.avast.sst.grpc.server.pureconfig

import com.avast.sst.grpc.server.GrpcServerConfig
import pureconfig.ConfigReader
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto._

trait ConfigReaders {

  implicit protected def hint[T]: ProductHint[T] = ProductHint.default

  implicit val grpcServerGrpcServerConfigReader: ConfigReader[GrpcServerConfig] = deriveReader[GrpcServerConfig]

}
