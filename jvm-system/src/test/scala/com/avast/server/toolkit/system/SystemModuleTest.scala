package com.avast.server.toolkit.system

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.concurrent.TimeUnit

import cats.effect.SyncIO
import org.scalatest.FunSuite

class SystemModuleTest extends FunSuite {

  test("SystemModule Clock") {
    val test = for {
      systemModule <- SystemModule.make[SyncIO]
      t1 <- systemModule.clock.monotonic(TimeUnit.NANOSECONDS)
      t2 <- systemModule.clock.monotonic(TimeUnit.NANOSECONDS)
    } yield assert(t1 <= t2)

    test.unsafeRunSync()
  }

  test("SystemModule Console input") {
    Console.withIn(new ByteArrayInputStream("test input\n".getBytes("UTF-8"))) {
      val test = for {
        systemModule <- SystemModule.make[SyncIO]
        line <- systemModule.console.readLine
      } yield assert(line == "test input")

      test.unsafeRunSync()
    }
  }

  test("SystemModule Console output") {
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      val test = for {
        systemModule <- SystemModule.make[SyncIO]
        _ <- systemModule.console.printLine("test output")
      } yield ()

      test.unsafeRunSync()
    }

    assert(out.toString("UTF-8") === "test output\n")
  }

  test("SystemModule Random") {
    val test = for {
      systemModule <- SystemModule.make[SyncIO]
      rnd <- systemModule.makeRandom(123L)
      number <- rnd.nextInt
    } yield assert(number === -1188957731L)

    test.unsafeRunSync()
  }

}
