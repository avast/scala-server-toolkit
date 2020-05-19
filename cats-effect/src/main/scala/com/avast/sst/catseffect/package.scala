package com.avast.sst

import java.util.concurrent.TimeUnit

import cats.effect.syntax.bracket._
import cats.effect.{Bracket, Clock}
import cats.syntax.flatMap._
import cats.syntax.functor._

import scala.concurrent.duration.Duration

package object catseffect {

  /**  */
  implicit class FOps[F[_], A](private val f: F[A]) extends AnyVal {

    /** Measures the time it takes the effect to finish and records it using the provided function. */
    def time(record: Duration => F[Unit])(implicit F: Bracket[F, Throwable], C: Clock[F]): F[A] = {
      val unit = TimeUnit.NANOSECONDS
      for {
        start <- C.monotonic(unit)
        result <- f.guarantee {
          C.monotonic(unit).map(computeTime(start)).flatMap(record)
        }
      } yield result
    }

    private def computeTime(start: Long)(end: Long) = Duration.fromNanos(end - start)

  }

}
