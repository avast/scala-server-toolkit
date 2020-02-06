---
layout: docs
title: "monix-catnap"
---

# monix-catnap

`libraryDependencies += "com.avast" %% "sst-monix-catnap" % "@VERSION@"`

This subproject provides interop between Scala Server Toolkit and [monix-catnap](https://monix.io/docs/3x/#monix-catnap) library.

## Circuit Breaker

You can use `CircuitBreakerModule` to instantiate and configure a circuit breaker and you can implement `CircuitBreakerMetrics` to get
monitoring of the circuit breaker. There is an implementation for Micrometer in `sst-monix-catnap-micrometer` subproject.

All of this is tied with http4s HTTP client in the `sst-http4s-client-monix-catnap` subproject so in practice you want to use
`Http4sClientCircuitBreakerModule` which wraps any `Client[F]` with a `CircuitBreaker` (it is recommended to have an enriched `CircuitBreaker`
with logging and metrics - see `CircuitBreakerModule` companion object methods). However the most important feature of the enriched circuit 
breaker is that any HTTP failure (according to `HttpStatusClassifier`) is converted to an exception internally which triggers the circuit 
breaking mechanism. Failing server is not overloaded by more requests and we do not have to wait for the response if the server is failing 
anyway.
