---
layout: home
title:  "Home"
---

This project is a culmination of years of Scala development at Avast and tries to represent the best practices of Scala server development 
we have gained together with tools that allow us to be effective. It is a set of small, flexible and cohesive building blocks that fit 
together well and allow you to build reliable server applications.

# Jump Right In

You can use the [official Giter8 template](https://github.com/avast/sst-seed.g8) to get started:

```bash
sbt new avast/sst-seed.g8
```

Or **[read documentation](getting-started)** or [deep dive into example code](https://github.com/avast/scala-server-toolkit/tree/master/example).

# SBT Dependency

```sbt
libraryDependencies += "com.avast" %% "sst-bundle-zio-http4s-blaze" % "@VERSION@"
```

# Resources

## Articles

* [Introducing Scala Server Toolkit](https://engineering.avast.io/introducing-scala-server-toolkit) (Avast Engineering)
* [SST - Creating HTTP Server](https://engineering.avast.io/scala-server-toolkit-creating-http-server) (Avast Engineering)

## Talks
* [Intro to Scala Server Toolkit](https://www.youtube.com/watch?v=T4xKu2bFUv0) (Functional JVM Meetup, Prague, January 23, 2020)
  * [slides](https://speakerdeck.com/jakubjanecek/intro-to-scala-server-toolkit)
