package com.avast.sst.catseffect

import cats.effect.syntax.bracket._
import cats.effect.{Bracket, Clock, ExitCase}
import cats.syntax.flatMap._
import cats.syntax.functor._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

object TimeUtils {

  private final val unit = TimeUnit.NANOSECONDS

  /** Measures the time it takes the effect to finish and records it using the provided function. */
  def time[F[_], A](f: F[A])(record: Duration => F[Unit])(implicit F: Bracket[F, Throwable], C: Clock[F]): F[A] = {
    for {
      start <- C.monotonic(unit)
      result <- f.guarantee {
        C.monotonic(unit).map(computeTime(start)).flatMap(record)
      }
    } yield result
  }

  /** Measures the time it takes the effect to finish and records it using the provided function. It distinguishes between successful
    * and failure state.
    * Please note, that in case of the effect cancellation the `record` is not invoked at all.
    */
  def timeCase[F[_], A](f: F[A])(record: Either[Duration, Duration] => F[Unit])(implicit F: Bracket[F, Throwable], C: Clock[F]): F[A] = {
    def calculateAndRecordAs(start: Long)(wrap: Duration => Either[Duration, Duration]): F[Unit] = {
      C.monotonic(unit).map(computeTime(start)).flatMap(d => record(wrap(d)))
    }

    F.bracketCase(C.monotonic(unit))(_ => f) {
      case (start, ExitCase.Completed) => calculateAndRecordAs(start)(Right(_))
      case (start, ExitCase.Error(_))  => calculateAndRecordAs(start)(Left(_))
      case _                           => F.unit
    }
  }

  private def computeTime(start: Long)(end: Long) = Duration.fromNanos(end - start)

}
