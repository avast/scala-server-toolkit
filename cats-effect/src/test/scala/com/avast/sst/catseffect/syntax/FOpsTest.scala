package com.avast.sst.catseffect.syntax

import cats.effect.concurrent.Ref
import cats.effect.{Clock, IO, Timer}
import com.avast.sst.catseffect.syntax.time._
import org.scalatest.funsuite.AsyncFunSuite

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{Duration, TimeUnit}

@SuppressWarnings(Array("scalafix:Disable.get", "scalafix:DisableSyntax.var"))
class FOpsTest extends AsyncFunSuite {

  implicit private val timer: Timer[IO] = IO.timer(ExecutionContext.global)

  test("time") {
    val sleepTime = Duration.fromNanos(500000000)
    implicit val mockClock: Clock[IO] = new Clock[IO] {
      var values = List(0L, sleepTime.toNanos)
      override def monotonic(unit: TimeUnit): IO[Long] = {
        val time = values.head
        values = values.tail
        IO.pure(time)
      }
      override def realTime(unit: TimeUnit): IO[Long] = ???
    }
    val io = for {
      ref <- Ref.of[IO, Option[Duration]](None)
      _ <- IO.sleep(sleepTime).time(d => ref.set(Some(d)))
      result <- ref.get
    } yield assert(result.isDefined && result.get.toMillis === sleepTime.toMillis)

    io.unsafeToFuture()
  }

  test("timeCase success") {
    val sleepTime = Duration.fromNanos(500000000)
    implicit val mockClock: Clock[IO] = new Clock[IO] {
      var values = List(0L, sleepTime.toNanos)
      override def monotonic(unit: TimeUnit): IO[Long] = {
        val time = values.head
        values = values.tail
        IO.pure(time)
      }
      override def realTime(unit: TimeUnit): IO[Long] = ???
    }

    val io = for {
      ref <- Ref.of[IO, Option[Either[Duration, Duration]]](None)
      _ <- IO.sleep(sleepTime).timeCase { eitherD =>
        ref.set(Some(eitherD))
      }
      result <- ref.get
    } yield assert(result === Some(Right(sleepTime)))

    io.unsafeToFuture()
  }

  test("timeCase failure") {
    val sleepTime = Duration.fromNanos(500000000)
    implicit val mockClock: Clock[IO] = new Clock[IO] {
      var values = List(0L, sleepTime.toNanos)
      override def monotonic(unit: TimeUnit): IO[Long] = {
        val time = values.head
        values = values.tail
        IO.pure(time)
      }
      override def realTime(unit: TimeUnit): IO[Long] = ???
    }

    val io = for {
      ref <- Ref.of[IO, Option[Either[Duration, Duration]]](None)
      _ <- IO.sleep(sleepTime)
      _ <- IO
        .raiseError(new RuntimeException("my exception"))
        .timeCase { eitherD =>
          ref.set(Some(eitherD))
        }
        .attempt
      result <- ref.get
    } yield assert(result === Some(Left(sleepTime)))

    io.unsafeToFuture()
  }

}
