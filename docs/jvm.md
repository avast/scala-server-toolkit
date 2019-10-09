# Module JVM

![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-jvm_2.12)

`libraryDependencies += "com.avast" %% "sst-jvm" % "<VERSION>"`

Module `sst-jvm` provides pure implementations of different JVM-related utilities:

* creation of thread pools,
* initialization of SSL context,
* standard in/out/err, random number generation,
* and more.

```scala
import com.avast.sst.jvm.system.console.ConsoleModule
import com.avast.sst.jvm.system.random.RandomModule
import zio.DefaultRuntime
import zio.interop.catz._
import zio.Task
 
val program = for {
  random <- RandomModule.makeRandom[Task]
  randomNumber <- random.nextInt
  console = ConsoleModule.make[Task]
  _ <- console.printLine(s"Random number: $randomNumber")
} yield ()
// program: zio.ZIO[Any, Throwable, Unit] = zio.ZIO$FlatMap@159314ca

val runtime = new DefaultRuntime {} // this is just needed in example
// runtime: AnyRef with DefaultRuntime = repl.Session$App$$anon$1@bafc754 // this is just needed in example
runtime.unsafeRun(program)
// Random number: 530700338
```

