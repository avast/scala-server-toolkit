package com.avast.server.toolkit.http4s

import cats.Functor
import org.http4s.syntax.kleisli._
import org.http4s.{HttpApp, HttpRoutes}

import scala.language.higherKinds

object Http4sRouting {

  /** Makes [[org.http4s.HttpApp]] from [[org.http4s.HttpRoutes]] */
  def make[F[_]: Functor](routes: HttpRoutes[F]): HttpApp[F] = routes.orNotFound

}
