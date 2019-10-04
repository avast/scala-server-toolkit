# Modules JVM

![Maven Central](https://img.shields.io/maven-central/v/com.avast/sst-jvm-system_2.12)

`libraryDependencies += "com.avast" %% "sst-jvm-system" % "<VERSION>"`

There is a set of `sst-jvm-*` modules that provide pure implementations of different JVM-related utilities:
 
 * `sst-jvm-execution` - creation of thread pools,
 * `sst-jvm-ssl` - initialization of SSL context,
 * `sst-jvm-system` - standard in/out/err, random number generation.
  
 ```scala mdoc
import com.avast.sst.system.console.ConsoleModule
import com.avast.sst.system.random.RandomModule
import zio.interop.catz._
import zio.DefaultRuntime
import zio.Task
 
val program = for {
  random <- RandomModule.makeRandom[Task]
  randomNumber <- random.nextInt
  console = ConsoleModule.make[Task]
  _ <- console.printLine(s"Random number: $randomNumber")
} yield ()

val runtime = new DefaultRuntime {} // this is just needed in example
runtime.unsafeRun(program)
 ```
