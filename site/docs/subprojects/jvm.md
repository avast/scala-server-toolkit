---
layout: docs
title: "JVM"
---

# JVM

`libraryDependencies += "com.avast" %% "sst-jvm" % "@VERSION@"`

Subproject `sst-jvm` provides pure implementations of different JVM-related utilities:
 
* creation of thread pools,
* standard in/out/err,
* and random number generation.
  
```scala mdoc:silent
import com.avast.sst.jvm.system.console.ConsoleModule
import com.avast.sst.jvm.system.random.RandomModule
import zio.interop.catz.*
import zio.Task
 
val program = for {
  random <- RandomModule.makeRandom[Task](1234L) // do not ever use seed like this!
  randomNumber <- random.nextInt
  console = ConsoleModule.make[Task]
  _ <- console.printLine(s"Random number: $randomNumber")
} yield ()

val runtime = zio.Runtime.default // this is just needed in example
```

```scala mdoc
runtime.unsafeRun(program)
```
