package com.avast.sst.grpc.server.pureconfig

import com.avast.sst.grpc.server.GrpcServerConfig
import pureconfig.ConfigReader

trait ConfigReaders {

  implicit val grpcServerGrpcServerConfigReader: ConfigReader[GrpcServerConfig] = implicitly[ConfigReader[GrpcServerConfig]]

}
