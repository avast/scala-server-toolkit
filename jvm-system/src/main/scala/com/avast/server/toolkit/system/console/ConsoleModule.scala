package com.avast.server.toolkit.system.console

import cats.effect.Sync

import scala.{Console => SConsole}

/** Provides console - standard in/out/err. */
object ConsoleModule {

  /** Makes [[com.avast.server.toolkit.system.console.Console]] with standard in/out/err. */
  def make[F[_]: Sync]: Console[F] = Console(SConsole.in, SConsole.out, SConsole.err)

}
