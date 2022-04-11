package com.avast.sst.jvm.system.console

import cats.effect.SyncIO
import org.scalatest.funsuite.AnyFunSuite

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import scala.Console as SConsole

class ConsoleModuleTest extends AnyFunSuite {

  test("Console input") {
    SConsole.withIn(new ByteArrayInputStream("test input\n".getBytes("UTF-8"))) {
      val test = for {
        line <- ConsoleModule.make[SyncIO].readLine
      } yield assert(line === "test input")

      test.unsafeRunSync()
    }
  }

  test("Console output") {
    val out = new ByteArrayOutputStream()
    SConsole.withOut(out) {
      val test = for {
        _ <- ConsoleModule.make[SyncIO].printLine("test output")
      } yield ()

      test.unsafeRunSync()
    }

    assert(out.toString("UTF-8") === "test output\n")
  }

  test("Console error") {
    val out = new ByteArrayOutputStream()
    SConsole.withErr(out) {
      val test = for {
        _ <- ConsoleModule.make[SyncIO].printLineToError("test output")
      } yield ()

      test.unsafeRunSync()
    }

    assert(out.toString("UTF-8") === "test output\n")
  }

}
