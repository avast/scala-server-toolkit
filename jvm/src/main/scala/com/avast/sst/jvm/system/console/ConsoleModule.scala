package com.avast.sst.jvm.system.console

import cats.effect.Sync

import scala.language.higherKinds
import scala.{Console => SConsole}

/** Provides console - standard in/out/err. */
object ConsoleModule {

  /** Makes [[com.avast.sst.jvm.system.console.Console]] with standard in/out/err. */
  def make[F[_]: Sync]: Console[F] = Console(SConsole.in, SConsole.out, SConsole.err)

}
