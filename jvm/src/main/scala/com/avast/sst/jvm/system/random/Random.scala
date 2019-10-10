package com.avast.sst.jvm.system.random

import cats.effect.Sync

import scala.language.higherKinds

/** Pure pseudo-random number generator based on the JVM implementation. */
trait Random[F[_]] {

  def nextDouble: F[Double]
  def nextBoolean: F[Boolean]
  def nextFloat: F[Float]
  def nextInt: F[Int]
  def nextInt(n: Int): F[Int]
  def nextLong: F[Long]
  def nextPrintableChar: F[Char]
  def nextString(length: Int): F[String]

}

object Random {

  def apply[F[_]: Sync](rnd: scala.util.Random): Random[F] = new Random[F] {

    private val F = Sync[F]

    override def nextDouble: F[Double] = F.delay(rnd.nextDouble())

    override def nextBoolean: F[Boolean] = F.delay(rnd.nextBoolean())

    override def nextFloat: F[Float] = F.delay(rnd.nextFloat())

    override def nextInt: F[Int] = F.delay(rnd.nextInt())

    override def nextInt(n: Int): F[Int] = F.delay(rnd.nextInt(n))

    override def nextLong: F[Long] = F.delay(rnd.nextLong())

    override def nextPrintableChar: F[Char] = F.delay(rnd.nextPrintableChar())

    override def nextString(length: Int): F[String] = F.delay(rnd.nextString(length))
  }

}
