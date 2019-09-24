# Modules JVM

![Maven Central](https://img.shields.io/maven-central/v/com.avast/scala-server-toolkit-jvm-system_2.13)

`libraryDependencies += "com.avast" %% "scala-server-toolkit-jvm-system" % "<VERSION>"`

There is a set of `scala-server-toolkit-jvm-*` modules that provide pure implementations of JVM-related utilities such as standard in/out,
time, random number generation, thread pools and SSL context initialization. The following modules are available:

* `scala-server-toolkit-jvm-execution`,
* `scala-server-toolkit-jvm-ssl`,
* `scala-server-toolkit-jvm-system`.

```scala
import java.util.concurrent.TimeUnit
import com.avast.server.toolkit.system.SystemModule
import zio.interop.catz._
import zio.Task
 
val program = for {
  systemModule <- SystemModule.make[Task]
  currentTime <- systemModule.clock.realTime(TimeUnit.MILLISECONDS)
  randomNumber <- systemModule.random.nextInt
  _ <- systemModule.console.printLine(s"Current Unix epoch time is $currentTime ms. Random number: $randomNumber")
} yield ()
// program: zio.ZIO[Any, Throwable, Unit] = zio.ZIO$FlatMap@7f42b194
```

