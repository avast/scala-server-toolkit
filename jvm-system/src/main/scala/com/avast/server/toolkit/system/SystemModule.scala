package com.avast.server.toolkit.system

import java.io.{OutputStream, Reader}
import java.security.SecureRandom

import cats.effect.{Clock, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.avast.server.toolkit.system.console.Console
import com.avast.server.toolkit.system.random.Random

import scala.language.higherKinds
import scala.{Console => SConsole}

/** Provides JVM system services such as standard in/out, time and random number generation.
  *
  * Contains already initialized standard [[com.avast.server.toolkit.system.console.Console]], default [[cats.effect.Clock]]
  * and [[com.avast.server.toolkit.system.random.Random]] or you can use `make*` factory methods to create more or customized instances.
  */
class SystemModule[F[_]: Sync](val console: Console[F], val clock: Clock[F], val random: Random[F]) {

  /** Makes [[Console]] with the provided input and output streams. */
  def makeConsole(in: Reader, out: OutputStream): F[Console[F]] = SystemModule.makeConsole(in, out)

  /** Makes [[Random]] with default random seed. */
  def makeRandom: F[Random[F]] = SystemModule.makeRandom

  /** Makes [[Random]] with the provided `seed`. */
  def makeRandom(seed: Long): F[Random[F]] = SystemModule.makeRandom(seed)

  /** Makes [[Random]] based on [[java.security.SecureRandom]] with default random seed. */
  def makeSecureRandom: F[Random[F]] = SystemModule.makeSecureRandom

  /** Makes [[Random]] based on [[java.security.SecureRandom]] with the provided `seed`. */
  def makeSecureRandom(seed: Array[Byte]): F[Random[F]] = SystemModule.makeSecureRandom(seed)

}

object SystemModule {

  /** Makes [[SystemModule]] with standard [[Console]], default [[cats.effect.Clock]]
    * and [[Random]] with default random seed.
    */
  def make[F[_]: Sync]: F[SystemModule[F]] = {
    for {
      console <- Sync[F].delay(Console(SConsole.in, SConsole.out))
      clock <- Sync[F].delay(Clock.create[F])
      random <- makeRandom[F]
    } yield new SystemModule(console, clock, random)
  }

  private def makeConsole[F[_]: Sync](in: Reader, out: OutputStream): F[Console[F]] = Sync[F].delay(Console(in, out))

  private def makeRandom[F[_]: Sync]: F[Random[F]] = Sync[F].delay(Random(new scala.util.Random()))

  private def makeRandom[F[_]: Sync](seed: Long): F[Random[F]] = Sync[F].delay(Random(new scala.util.Random(seed)))

  private def makeSecureRandom[F[_]: Sync]: F[Random[F]] = Sync[F].delay(Random(new SecureRandom()))

  private def makeSecureRandom[F[_]: Sync](seed: Array[Byte]): F[Random[F]] = Sync[F].delay(Random(new SecureRandom(seed)))

}
