package com.avast.sst.system.console

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import cats.effect.SyncIO
import org.scalatest.FunSuite

import scala.{Console => SConsole}

class ConsoleModuleTest extends FunSuite {

  test("Console input") {
    SConsole.withIn(new ByteArrayInputStream("test input\n".getBytes("UTF-8"))) {
      val test = for {
        line <- ConsoleModule.make[SyncIO].readLine
      } yield assert(line == "test input")

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
