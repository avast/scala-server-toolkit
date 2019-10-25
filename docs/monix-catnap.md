# monix-catnap

[![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-monix-catnap_2.12)](https://repo1.maven.org/maven2/com/avast/sst-monix-catnap_2.12/)

`libraryDependencies += "com.avast" %% "sst-monix-catnap" % "<VERSION>"`

This module provides interop between Scala Server Toolkit and [monix-catnap](https://monix.io/docs/3x/#monix-catnap) library.

## Circuit Breaker

You can use `CircuitBreakerModule` to instantiate and configure a circuit breaker and you can implement `CircuitBreakerMetrics` to get
monitoring of the circuit breaker. There is an implementation for Micrometer in `sst-monix-catnap-micrometer` module.

All of this is tied with http4s HTTP client in the `sst-http4s-client-monix-catnap-micrometer` module so in practice you want to use
`Http4sClientCircuitBreakerModule` which wraps any `Client[F]` with the provided `CircuitBreaker` and adds logging/metrics. However
the most important feature of the enriched circuit breaker is that any HTTP failure (response `5xx`) is converted to an exception internally
which triggers the circuit breaking mechanism. Failing server is not overloaded by more requests and we do not have to wait for the response
if the server is failing anyway.
