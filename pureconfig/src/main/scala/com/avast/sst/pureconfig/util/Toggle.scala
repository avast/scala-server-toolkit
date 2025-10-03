package com.avast.sst.pureconfig.util

import cats.{FlatMap, Functor, Monad, Monoid, Order, Semigroup}

import java.util.Collections
import com.typesafe.config.ConfigValueFactory
import pureconfig.{ConfigReader, ConfigWriter}

import scala.annotation.tailrec

sealed trait Toggle[+T] {
  def toOption: Option[T]
  def fold[A](empty: => A, fromValue: T => A): A
  def isEmpty: Boolean
}

object Toggle {
  final case class Enabled[+T](value: T) extends Toggle[T] {
    override def toOption: Option[T] = Some(value)
    override def fold[A](empty: => A, fromValue: T => A): A = fromValue(value)
    override def isEmpty: Boolean = false
    def get: T = value
  }
  case object Disabled extends Toggle[Nothing] {
    override def toOption: Option[Nothing] = None
    override def fold[A](empty: => A, fromValue: Nothing => A): A = empty
    override def isEmpty: Boolean = true
  }

  object TogglePureConfigInstances {
    implicit def toggleConfigReader[T: ConfigReader]: ConfigReader[Toggle[T]] = {
      ConfigReader
        .forProduct1[ConfigReader[Toggle[T]], Boolean]("enabled") { enabled =>
          if (enabled) implicitly[ConfigReader[T]].map(Enabled[T])
          else ConfigReader.fromCursor(_ => Right(Disabled))
        }
        .flatMap(identity)
    }

    implicit def toggleConfigWriter[T: ConfigWriter]: ConfigWriter[Toggle[T]] = {
      ConfigWriter.fromFunction[Toggle[T]] {
        case Enabled(value) => ConfigWriter[T].to(value).withFallback(ConfigValueFactory.fromMap(Collections.singletonMap("enabled", true)))
        case Disabled       => ConfigValueFactory.fromMap(Collections.singletonMap("enabled", false))
      }
    }
  }

  object ToggleStdInstances {
    implicit val functorForToggle: Functor[Toggle] = new ToggleFunctor
    implicit val flatMapForToggle: FlatMap[Toggle] = new ToggleFlatMap
    implicit val monadForToggle: Monad[Toggle] = new ToggleMonad
    implicit def monoidForToggle[A: Semigroup]: Monoid[Toggle[A]] = new ToggleMonoid[A]
    implicit def orderForToggle[A: Order]: Order[Toggle[A]] = new ToggleOrder[A]
  }

  class ToggleFunctor extends Functor[Toggle] {
    override def map[A, B](fa: Toggle[A])(f: A => B): Toggle[B] = {
      fa match {
        case Enabled(value) => Enabled(f(value))
        case Disabled       => Disabled
      }
    }
  }

  class ToggleFlatMap extends ToggleFunctor with FlatMap[Toggle] {
    override def flatMap[A, B](fa: Toggle[A])(f: A => Toggle[B]): Toggle[B] = {
      fa match {
        case Enabled(value) => f(value)
        case Disabled       => Disabled
      }
    }

    override def tailRecM[A, B](a: A)(f: A => Toggle[Either[A, B]]): Toggle[B] = tailRecMPrivate(a)(f)

    @tailrec
    private def tailRecMPrivate[A, B](a: A)(f: A => Toggle[Either[A, B]]): Toggle[B] = {
      f(a) match {
        case Enabled(Left(value))  => tailRecMPrivate(value)(f)
        case Enabled(Right(value)) => Enabled(value)
        case Disabled              => Disabled
      }
    }
  }

  class ToggleMonad extends ToggleFlatMap with Monad[Toggle] {
    override def pure[A](x: A): Toggle[A] = Enabled(x)
  }

  class ToggleMonoid[A](implicit A: Semigroup[A]) extends Monoid[Toggle[A]] {
    override def empty: Toggle[A] = Disabled
    override def combine(x: Toggle[A], y: Toggle[A]): Toggle[A] =
      x match {
        case Disabled   => y
        case Enabled(a) =>
          y match {
            case Disabled   => x
            case Enabled(b) => Enabled(A.combine(a, b))
          }
      }
  }

  class ToggleOrder[A](implicit A: Order[A]) extends Order[Toggle[A]] {
    override def compare(x: Toggle[A], y: Toggle[A]): Int =
      x match {
        case Disabled   => if (y.isEmpty) 0 else -1
        case Enabled(a) =>
          y match {
            case Disabled   => 1
            case Enabled(b) => A.compare(a, b)
          }
      }
  }

}
