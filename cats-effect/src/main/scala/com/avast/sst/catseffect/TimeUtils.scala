package com.avast.sst.catseffect

import cats.effect.syntax.bracket._
import cats.effect.{Bracket, Clock}
import cats.syntax.flatMap._
import cats.syntax.functor._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

object TimeUtils {

  /** Measures the time it takes the effect to finish and records it using the provided function. */
  def time[F[_], A](f: F[A])(record: Duration => F[Unit])(implicit F: Bracket[F, Throwable], C: Clock[F]): F[A] = {
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
