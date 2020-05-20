package com.avast.sst.catseffect.syntax

import cats.effect.{Bracket, Clock}
import com.avast.sst.catseffect.TimeUtils
import com.avast.sst.catseffect.syntax.TimeSyntax.FOps

import scala.concurrent.duration.Duration

trait TimeSyntax {

  @SuppressWarnings(Array("scalafix:DisableSyntax.implicitConversion"))
  implicit def sstFOps[F[_], A](f: F[A]): FOps[F, A] = new FOps(f)

}

object TimeSyntax {

  final class FOps[F[_], A](private val f: F[A]) extends AnyVal {

    /** Measures the time it takes the effect to finish and records it using the provided function. */
    def time(record: Duration => F[Unit])(implicit F: Bracket[F, Throwable], C: Clock[F]): F[A] = TimeUtils.time(f)(record)

  }

}
