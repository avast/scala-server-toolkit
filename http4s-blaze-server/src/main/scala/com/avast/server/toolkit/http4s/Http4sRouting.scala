package com.avast.server.toolkit.http4s

import cats.Monad
import cats.syntax.all._
import org.http4s.syntax.kleisli._
import org.http4s.{HttpApp, HttpRoutes}

import scala.language.higherKinds

object Http4sRouting {

  /** Makes [[org.http4s.HttpApp]] from [[org.http4s.HttpRoutes]] */
  def make[F[_]: Monad](routes: HttpRoutes[F], more: HttpRoutes[F]*): HttpApp[F] = {

    more
      .foldLeft[HttpRoutes[F]](routes)(_.combineK(_))
      .orNotFound
  }

}
