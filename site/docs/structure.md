---
layout: docs
title: "Structure"
position: 2
---

# Structure

The project is split into many small subprojects based on dependencies. For example code related to loading of configuration files via
[PureConfig](https://pureconfig.github.io) lives in subproject named `sst-pureconfig` and code related to http4s server implemented using
[Blaze](https://github.com/http4s/blaze) lives in subproject named `sst-http4s-server-blaze`.

There are also subprojects that implement interoperability between usually two dependencies. For example we want to configure our HTTP server
using PureConfig so definition of `implicit` `ConfigReader` instances lives in subproject named `sst-http4s-server-blaze-pureconfig`. Or to give
another example, monitoring of HTTP server using [Micrometer](https://micrometer.io) lives in subproject named `sst-http4s-server-micrometer`.
Note that such subproject depends on APIs of both http4s server and Micrometer but it does not depend on concrete implementation which allows
you to choose any http4s implementation (Blaze, ...) and any Micrometer implementation (JMX, StatsD, ...).
