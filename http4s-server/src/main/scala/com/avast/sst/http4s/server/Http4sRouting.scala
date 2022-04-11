package com.avast.sst.http4s.server

import cats.Monad
import cats.syntax.all.*
import org.http4s.{HttpApp, HttpRoutes}

object Http4sRouting {

  /** Makes [[org.http4s.HttpApp]] from [[org.http4s.HttpRoutes]] */
  def make[F[_]: Monad](routes: HttpRoutes[F], more: HttpRoutes[F]*): HttpApp[F] = {
    more
      .foldLeft[HttpRoutes[F]](routes)(_.combineK(_))
      .orNotFound
  }

}
