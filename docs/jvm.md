# Modules JVM

![Maven Central](https://img.shields.io/maven-central/v/com.avast/scala-server-toolkit-jvm-system_2.12)

`libraryDependencies += "com.avast" %% "scala-server-toolkit-jvm-system" % "<VERSION>"`

There is a set of `scala-server-toolkit-jvm-*` modules that provide pure implementations of different JVM-related utilities:

* `scala-server-toolkit-jvm-execution` - creation of thread pools,
* `scala-server-toolkit-jvm-ssl` - initialization of SSL context,
* `scala-server-toolkit-jvm-system` - standard in/out/err, random number generation.

```scala
import com.avast.server.toolkit.system.console.ConsoleModule
import com.avast.server.toolkit.system.random.RandomModule
import zio.interop.catz._
import zio.DefaultRuntime
import zio.Task
 
val program = for {
  random <- RandomModule.makeRandom[Task]
  randomNumber <- random.nextInt
  console = ConsoleModule.make[Task]
  _ <- console.printLine(s"Random number: $randomNumber")
} yield ()
// program: zio.ZIO[Any, Throwable, Unit] = zio.ZIO$FlatMap@7b5833ee

val runtime = new DefaultRuntime {} // this is just needed in example
// runtime: AnyRef with DefaultRuntime = repl.Session$App$$anon$1@3b362f1 // this is just needed in example
runtime.unsafeRun(program)
// Random number: -1757646767
```

