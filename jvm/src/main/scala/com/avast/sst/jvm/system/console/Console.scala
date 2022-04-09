package com.avast.sst.jvm.system.console

import cats.effect.Sync

import java.io.{OutputStream, Reader}
import scala.io.StdIn
import scala.{Console as SConsole}

/** Pure console allowing to read and print lines. */
trait Console[F[_]] {

  def printLine(value: String): F[Unit]

  def printLineToError(value: String): F[Unit]

  def readLine: F[String]

}

object Console {

  def apply[F[_]: Sync](in: Reader, out: OutputStream, err: OutputStream): Console[F] =
    new Console[F] {

      private val F = Sync[F]

      override def printLine(value: String): F[Unit] =
        F.delay {
          SConsole.withOut(out)(SConsole.println(value))
        }

      override def printLineToError(value: String): F[Unit] =
        F.delay {
          SConsole.withErr(err)(SConsole.err.println(value))
        }

      override def readLine: F[String] =
        F.delay {
          SConsole.withIn(in)(StdIn.readLine())
        }
    }

}
