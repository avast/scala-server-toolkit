package com.avast.sst.grpc.server.interceptor

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener
import io.grpc.*
import org.slf4j.Logger

/** Adds basic logging around each gRPC call. */
class LoggingServerInterceptor(logger: Logger) extends ServerInterceptor {

  override def interceptCall[ReqT, RespT](
      call: ServerCall[ReqT, RespT],
      headers: Metadata,
      next: ServerCallHandler[ReqT, RespT]
  ): ServerCall.Listener[ReqT] = {
    val methodName = call.getMethodDescriptor.getFullMethodName
    val finalCall = new CloseServerCall(methodName, call)
    new OnMessageServerCallListener(methodName, next.startCall(finalCall, headers))
  }

  private class CloseServerCall[A, B](methodName: String, delegate: ServerCall[A, B]) extends SimpleForwardingServerCall[A, B](delegate) {
    override def close(status: Status, trailers: Metadata): Unit = {
      import io.grpc.Status
      if ((status.getCode eq Status.Code.UNKNOWN) || (status.getCode eq Status.Code.INTERNAL)) {
        logger.error(
          String.format(
            "Error response from method %s: %s %s",
            methodName,
            status.getCode,
            status.getDescription
          ),
          status.getCause
        )
      } else if (!status.isOk) {
        logger.warn(
          String.format(
            "Error response from method %s: %s %s",
            methodName,
            status.getCode,
            status.getDescription
          ),
          status.getCause
        )
      } else {
        logger.debug("Successful response from method {}: {}", Array(methodName, status)*)
      }
      super.close(status, trailers)
    }
  }

  private class OnMessageServerCallListener[A](methodName: String, delegate: ServerCall.Listener[A])
      extends SimpleForwardingServerCallListener[A](delegate) {
    override def onMessage(message: A): Unit = {
      logger.debug("Dispatching method {}", methodName)
      super.onMessage(message)
    }
  }

}
