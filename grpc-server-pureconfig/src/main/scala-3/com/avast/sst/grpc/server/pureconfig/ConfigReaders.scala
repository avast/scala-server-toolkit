package com.avast.sst.grpc.server.pureconfig

import com.avast.sst.grpc.server.GrpcServerConfig
import pureconfig.ConfigReader
import pureconfig.generic.derivation.default.*

trait ConfigReaders {

  implicit val grpcServerGrpcServerConfigReader: ConfigReader[GrpcServerConfig] = ConfigReader.derived

}
